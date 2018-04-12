/**
 * @author Nick Liccini
 *
 * array_list.c:
 */

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "list.h"
#include "array_list.h"


/**
 * Creates an empty array list
 * - array list's size should be 0, cuz it's empty
 * - allocates a backing array of size INITIAL_CAPACITY
 * - array list's capacity should be the size of the backing array
 * - the backing array should be empty (all entries should be NULL)
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 *
 * @return the newly created array list
 */
array_list_t* create_array_list(void)
{
    array_list_t* array_list;

    // Allocate memory for the array list struct
    array_list = (array_list_t*) malloc(sizeof(array_list_t));
    if (!array_list) {
        return NULL;
    }

    // Set the initial struct members
    array_list->size = 0;
    array_list->capacity = INITIAL_CAPACITY;

    // Allocate memory for the backing array
    array_list->entries = malloc(INITIAL_CAPACITY * sizeof(void*));
    if (!array_list->entries) {
        free(array_list);
        return NULL;
    }

    // Set all the entries to NULL
    memset(array_list->entries, 0, INITIAL_CAPACITY * sizeof(void*));

    return array_list;
}

/**
 * Destroys an array list
 * - all memory allocated to the array list should be freed
 * - all memory allocated to each data entry in the array should be freed
 * - return non-zero if either parameter is NULL
 *
 * @param arr array list that you're destroying
 * @param free_func function that frees a data pointer
 * @return 0 if operation succeeded, non-zero otherwise
 */
int destroy_array_list(array_list_t* arr, list_op free_func)
{
    if (arr == NULL || free_func == NULL) {
        return 1;
    }

    // Free every non-NULL element in the backing array
    for (int i = 0; i < arr->size; ++i) {
        if (arr->entries[i] != NULL) {
            (*free_func)(arr->entries[i]);
        }
    }

    // Free the backing array
    free(arr->entries);

    // Free the array list struct
    free(arr);

    return 0;
}

/**
 * Copies the array list (shallow copy)
 * - creates copy of the array_list_t struct
 * - creates copy of the array list's backing array
 * - DOES NOT create a copy of data pointed at by each data pointer
 * - if list_to_copy is NULL, return NULL
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 * - list_to_copy should not be changed by this function
 *
 * @param list_to_copy array list you're copying
 * @return the shallow copy of the given array list
 */
array_list_t* shallow_copy_array_list(array_list_t *list_to_copy)
{
    if (list_to_copy == NULL) {
        return NULL;
    }

    // Allocate memory for a new array list struct
    array_list_t* list_to_return;
    list_to_return = (array_list_t*) malloc(sizeof(array_list_t));
    if (!list_to_return) {
        return NULL;
    }

    // Set the struct members
    list_to_return->size = list_to_copy->size;
    list_to_return->capacity = list_to_copy->capacity;

    // Allocate memory for a new backing array
    list_to_return->entries = malloc(list_to_copy->capacity * sizeof(void*));
    if (!list_to_return->entries) {
        free(list_to_return);
        return NULL;
    }

    // Use the same memory from list_to_copy->entries for the list_to_return
    memcpy(list_to_return->entries, list_to_copy->entries, list_to_copy->capacity * sizeof(void*));

    return list_to_return;
}


/**
 * Copies the array list (deep copy)
 * - creates copy of the array_list_t struct
 * - creates copy of the array list's backing array
 * - create a copy of data pointed at by each data pointer
 * - return NULL if any parameters are NULL
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 * - list_to_copy should not be changed by this function
 *
 * @param list_to_copy array list you're copying
 * @param copy_func function that returns a copy of the given data pointer
 * @param free_func function that frees a given data pointer
 * @return the deep copy of the given array list
 */
array_list_t* deep_copy_array_list(array_list_t *list_to_copy, list_copy copy_func, list_op free_func)
{
    if (list_to_copy == NULL || copy_func == NULL || free_func == NULL) {
        return NULL;
    }

    // Allocate memory for a new array list struct
    array_list_t* list_to_return;
    list_to_return = (array_list_t*) malloc(sizeof(array_list_t));
    if (!list_to_return) {
        return NULL;
    }

    // Copy the struct members
    list_to_return->size = list_to_copy->size;
    list_to_return->capacity = list_to_copy->capacity;

    // Allocate memory for a new backing array
    list_to_return->entries = malloc(list_to_return->capacity * sizeof(void*));
    if (!list_to_return->entries) {
        free(list_to_return);
        return NULL;
    }

    // Go through each element in list_to_copy
    void** new_data = malloc(sizeof(void*));
    if (!new_data) {
        free(list_to_return->entries);
        free(list_to_return);
        return NULL;
    }
    for (int i = 0; i < list_to_return->capacity; ++i) {
        // Allocate memory for each element (using copy_func)
        if (list_to_copy->entries[i] == NULL) {
            list_to_return->entries[i] = NULL;
        } else {
            bool bad_copy = (*copy_func)(list_to_copy->entries[i], new_data);
            if (bad_copy) {
                for (int j = 0; j < i; ++j) {
                    (*free_func)(list_to_return->entries[j]);
                }
                free(list_to_return->entries);
                free(list_to_return);
                return NULL;
            }
            list_to_return->entries[i] = *new_data;
        }
    }
    free(new_data);

    return list_to_return;
}

/**
 * Adds the given data at the given index
 * - if the index is negative, wrap around (ex: -1 becomes size - 1)
 * - if size + index < 0, keep wrapping around until a positive in bounds index is reached
 *   (ex: -5 with size 4 becomes index 4)
 * - adding in the middle of the array list (index < size)
 *     - increment size
 *     - if necessary, resize the backing array as described below
 *     - shift all elements with indices [index, size - 1] down by one
 *     - the indices of the shifted elements should now be [index + 1, size])
 *     - put data into index in the array list
 * - adding at the end of the array list (index == size)
 *     - increment size
 *     - if necessary, resize the backing array as described below
 *     - put data into the index in the array list
 * - adding after the end of the array list (index > size)
 *     - index now represents the last element in the array list, so size = index + 1
 *     - if necessary, resize the backing array as described below
 *     - put data into the index in the array list
 *     - Note: this operation will result in 1 or more empty slots from [old size, index - 1]
 * - if any of these operations would increase size to size >= capacity, you must
 *   increase the size of the backing array by GROWTH_FACTOR until size < capacity.
 *   Only then can you insert into the backing array
 * - if any memory allocation operations fail, do not change the array list and return non-zero
 *     - the array list should be in the same state it was in before add_to_array_list was called
 * - if arr is NULL, return non-zero
 * - data can be NULL
 *
 * @param arr array list you're adding to
 * @param index you're adding data to
 * @param data data you're putting into the array list
 * @return 0 if add operation succeeded, non-zero otherwise
 */
int add_to_array_list(array_list_t* arr, int index, void* data)
{
    if (arr == NULL) {
        return 1;
    }

    void** ptr = NULL;
    int oldSize = arr->size;

    // Wrap index around if applicable
    while (index < 0) {
        index = arr->size + index;
    }
    while (arr->size + index < 0) {
        index = arr->size + index;
    }


    // Add
    if (index < arr->size) { // Adding to middle
        arr->size++;

        // Grow backing array
        if (arr->size >= arr->capacity) {
            // Grow the capacity
            while (arr->size >= arr->capacity) {
                arr->capacity += GROWTH_FACTOR;
            }

            // Get more memory for the backing array
            ptr = realloc(arr->entries, arr->capacity * sizeof(void*));
            if (!ptr) {
                arr->size--;
                return 1;
            }

            // Set the additional space to NULL
            memset(ptr + oldSize, 0, (arr->capacity - oldSize) * sizeof(void*));

            // Update the backing array pointer
            arr->entries = ptr;
        }

        // Shift entries[index:length-1] to entries[index+1:length]
        memmove(arr->entries + index + 1, arr->entries + index, (arr->size - index) * sizeof(void*));

        // Insert data to entries[index]
        arr->entries[index] = data;
    } else if (index == arr->size) { // Add to end
        arr->size++;

        // Grow backing array
        if (arr->size >= arr->capacity) {
            // Grow the capacity
            while (arr->size >= arr->capacity) {
                arr->capacity += GROWTH_FACTOR;
            }

            // Get more memory for the backing array
            ptr = realloc(arr->entries, arr->capacity * sizeof(void*));
            if (!ptr) {
                arr->size--;
                return 1;
            }


            // Set the additional space to NULL
            memset(ptr + oldSize, 0, (arr->capacity - oldSize) * sizeof(void*));

            // Update the backing array pointer
            arr->entries = ptr;
        }

        // Insert data to entries[index]
        arr->entries[index] = data;
    } else if (index > arr->size) { // Add out of bounds
        arr->size = index + 1;

        // Grow backing array
        if (arr->size >= arr->capacity) {
            // Grow the capacity
            while (arr->size >= arr->capacity) {
                arr->capacity += GROWTH_FACTOR;
            }

            // Get more memory for the backing array
            ptr = realloc(arr->entries, arr->capacity * sizeof(void*));
            if (!ptr) {
                arr->size--;
                return 1;
            }

            // Set the additional space to NULL
            memset(ptr + oldSize, 0, (arr->capacity - oldSize) * sizeof(void*));

            // Update the backing array pointer
            arr->entries = ptr;
        }

        // Insert data to entries[index]
        arr->entries[index] = data;
    }

    return 0;
}


/**
 * Removes the data at the given index and returns it to the user
 * - if the index is negative, wrap around (ex: -1 becomes size - 1)
 * - if size + index < 0, keep wrapping around until a positive in bounds index is reached
 *   (ex: -5 with size 4 becomes index 4)
 * - removing from the middle of the array list (index < size - 1)
 *     - save the data pointer at index
 *     - shift all elements with indices [index + 1, size - 1] up by one
 *     - the indices of the shifted elements should now be [index, size - 2])
 *     - decrement the size and resize backing array if necessary
 *     - return the saved data pointer
 * - removing from the end of the array list (index == size - 1)
 *     - save the data pointer at index
 *     - decrement the size and resize backing array if necessary
 *     - return the saved data pointer
 * - removing from after the end of the array list (index > size - 1)
 *     - there's nothing to remove, so fail by returning NULL
 * - if any of these operations would decrease size to size < capacity / GROWTH_FACTOR ^ 2, you must
 *   shrink the backing array by GROWTH_FACTOR so that capacity = previous capacity / GROWTH_FACTOR
 * - if arr or data_out are NULL, return non-zero
 *
 * @param arr array list you're removing from
 * @param index you're removing from array list
 * @param data_out pointer where you store data pointer removed from array list
 * @return 0 if remove operation succeeded, non-zero otherwise
 */
int remove_from_array_list(array_list_t* arr, int index, void** data_out)
{
    if (arr == NULL || data_out == NULL) {
        return 1;
    }

    void** ptr = NULL;

    // Wrap index around if negative
    while (index < 0) {
        index = arr->size + index;
    }
    while (arr->size + index < 0) {
        index = arr->size + index;
    }

    if (index < arr->size - 1) { // Remove from the middle
        // Save the data at entries[index]
        *data_out = arr->entries[index];

        // Shift all elements up one
        memmove(arr->entries + index, arr->entries + index + 1, (arr->size - index) * sizeof(void*));

        // Decrement size
        arr->size--;

        // Shrink backing array
        if (arr->size < (arr->capacity / (GROWTH_FACTOR * GROWTH_FACTOR))) {
            while (arr->size < (arr->capacity / (GROWTH_FACTOR * GROWTH_FACTOR))) {
                arr->capacity = arr->capacity / GROWTH_FACTOR;
            }
            ptr = realloc(arr->entries, arr->capacity * sizeof(void*));
            if (!ptr) {
                arr->size++;
                return 1;
            }
            arr->entries = ptr;
        }
    } else if (index == arr->size - 1) { // Remove from the end
        // Save the data at entries[index]
        *data_out = arr->entries[index];
        arr->entries[index] = NULL;

        // Decrement
        arr->size--;

        // Shrink backing array
        if (arr->size < (arr->capacity / (GROWTH_FACTOR * GROWTH_FACTOR))) {
            while (arr->size < (arr->capacity / (GROWTH_FACTOR * GROWTH_FACTOR))) {
                arr->capacity = arr->capacity / GROWTH_FACTOR;
            }
            ptr = realloc(arr->entries, arr->capacity * sizeof(void*));
            if (!ptr) {
                arr->size++;
                return 1;
            }
            arr->entries = ptr;
        }
    } else if (index > arr->size - 1) { // Nothing here
        return 1;
    }

    return 0;
}


/**
 * Checks if array list contains data
 * - checks if each data pointer from indices [0, size - 1]
 *   equal the passed in data using eq_func
 * - terminate search on first match
 * - if data is found, return it through return_found
 * - if any parameters besides data are NULL, return false
 *
 * @param arr array list you're searching
 * @param data the data you're looking for
 * @param eq_func function used to check if two data pointers are equal  (returns non-zero if equal)
 * @param return_found pointer through which you return found data if found
 * @return true if array list contains data
 */
bool array_list_contains(array_list_t* arr, void* data, list_eq eq_func, void** return_found)
{
    bool found_item = false;

    if (arr == NULL || eq_func == NULL || return_found == NULL) {
        return found_item;
    }

    for (int i = 0; i < arr->size; ++i) {
        void* entry = arr->entries[i];
        if ((*eq_func)(entry, data)) {
            *return_found = entry;
            found_item = true;
            break;
        }
    }

    return found_item;
}

/**
 * Trims backing array's size so it's the same as the size of the array
 * - if any memory allocation operations fail, do not change the array list and return non-zero
 * - the array list should be in the same state it was in before trim_to_size was called
 * - if arr is NULL, return non-zero
 *
 * @param arr array list who's backing array you're trimming
 * @return 0 if remove operation succeeded, non-zero otherwise
 */
int trim_to_size(array_list_t* arr)
{
    if (arr == NULL) {
        return 1;
    }

    void* ptr = NULL;
    ptr = realloc(arr->entries, arr->size * sizeof(void*));
    if (!ptr) {
        return 1;
    }
    arr->entries = ptr;
    arr->capacity = arr->size;

    return 0;
}
