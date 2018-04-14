/**
 * @author Nick Liccini
 *
 * circ_list.c: fill in all functions
 */

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "list.h"
#include "circ_list.h"


/**
 * Creates a linked list node
 * - node should contain the data that's passed in
 * - next and prev pointers should be NULL
 *   (this node should not be part of any list)
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 * - data can be NULL
 *
 * @param data you're putting into this node
 * @return the newly created linked list node
 */
static l_node *create_node(void *data)
{
    l_node* new_node;
    new_node = (l_node*) malloc(sizeof(l_node*));
    if (!new_node) {
        return NULL;
    }
    new_node->data = data;
    new_node->prev = NULL;
    new_node->next = NULL;

    return new_node;
}

/**
 * Creates an empty linked list
 * - linked list's size should be 0 (cuz it's empty)
 * - linked list's head should be NULL (cuz it's empty)
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 *
 * @return the newly created linked list
 */
circ_list *create_linked_list(void)
{
    circ_list* new_list;
    new_list = (circ_list*) malloc(sizeof(circ_list*));
    if (!new_list) {
        return NULL;
    }
    new_list->size = 0;
    new_list->head = NULL;

    return new_list;
}

/**
 * Destroys a linked list
 * - all memory allocated to the linked list should be freed
 * - all memory allocated to each data entry in the linked list should be freed
 * - return non-zero if either parameters is NULL
 *
 * @param arr linked list that you're destroying
 * @param free_func function that frees a data pointer
 * @return 0 if operation succeeded, non-zero otherwise
 */
int destroy_linked_list(circ_list* list, list_op free_func)
{
    if (list == NULL || free_func == NULL) {
        return 1;
    }

    l_node* curr = list->head;
    l_node* next = NULL;

    if (curr != NULL) {
        next = curr->next;
        (*free_func)(curr->data);
        free(curr);
        curr = next;

        for (int i = 1; i < list->size; ++i) {
            next = curr->next;
            (*free_func)(curr->data);
            free(curr);
            curr = next;
        }
    }
    

    free(list);

    return 0;
}


/**
 * Copies the linked list (shallow copy)
 * - creates a copy of the circ_list struct
 * - creates a copy of each of the linked list's nodes
 * - DOES NOT create a copy of the data pointed at by each data pointer
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 * - list_to_copy should not be changed by this function
 * - if list_to_copy is NULL, return NULL
 *
 * @param list_to_copy linked list you're copying
 * @return the shallow copy of the given linked list
 */
circ_list *shallow_copy_linked_list(circ_list *list_to_copy)
{
    if (list_to_copy == NULL) {
        return NULL;
    }

    circ_list* new_list;
    l_node* new_curr = NULL;
    l_node* copy_curr = list_to_copy->head;

    // Create new list
    new_list = create_linked_list();
    if (!new_list) {
        return NULL;
    }
    new_list->size = list_to_copy->size;
    if (list_to_copy->head == NULL) {
        return new_list;
    }

    // Create new head and add it to the new list
    new_curr = create_node(copy_curr->data);
    new_list->head = new_curr;
    new_list->head->next = new_list->head;
    new_list->head->prev = new_list->head;

    // Create new nodes and add them to the new head
    if (copy_curr != NULL) {
        copy_curr = copy_curr->next;
        l_node* new_next = NULL;
        for (int i = 1; i < list_to_copy->size; ++i) {
            new_next = create_node(copy_curr->data);
            if (!new_next) {
                // free all previously allocated new nodes
                new_curr = new_list->head;
                for (int j = 0; j < i; ++j) {
                    new_next = new_curr->next;
                    free(new_curr);
                    new_curr = new_next;
                }
                // free the new list
                free(new_list);
                return NULL;
            }

            // Set the node pointers
            new_curr->next = new_next;
            new_next->prev = new_curr;

            // Update the loop parameters
            new_curr = new_next;
            copy_curr = copy_curr->next;
        }
    }

    // Set the head pointers
    new_curr->next = new_list->head;
    new_list->head->prev = new_curr;

    return new_list;
}


/**
 * Creates a copy of the linked list (deep copy)
 * - creates a copy of the circ_list struct
 * - creates a copy of each of the linked list's nodes
 * - creates a copy of the data pointed at by each data pointer
 * - if any memory allocation operations fail, return NULL and
 *   clean up any dangling pointers
 * - list_to_copy should not be changed by this function
 * - if any parameters are NULL, return NULL
 *
 * @param list_to_copy linked list you're copying
 * @param copy_func function that returns a copy of the given data pointer
 * @param free_func function that frees a given data pointer
 * @return the deep copy of the given linked list
 */
circ_list *deep_copy_linked_list(circ_list *list_to_copy, list_copy copy_func, list_op free_func)
{
    if (list_to_copy == NULL) {
        return NULL;
    }

    circ_list* new_list;
    l_node* new_curr;
    l_node* new_next;
    l_node* copy_curr = list_to_copy->head;
    void** new_data = malloc(sizeof(void*));
    if (!new_data) {
        return NULL;
    }
    (*copy_func)(copy_curr->data, new_data);

    // Create new list
    new_list = create_linked_list();
    if (!new_list) {
        return NULL;
    }
    new_list->size = list_to_copy->size;

    // Create new head and add it to the new list
    new_curr = create_node(*new_data);
    if (!new_curr) {
        (*free_func)(new_data);
        free(new_list);
        return NULL;
    }
    new_list->head = new_curr;
    new_list->head->next = new_list->head;
    new_list->head->prev = new_list->head;

    // Create new nodes and add them to the new head
    copy_curr = copy_curr->next;
    for (int i = 1; i < list_to_copy->size; ++i) {
        (*copy_func)(copy_curr->data, new_data);
        new_next = create_node(*new_data);
        if (!new_next) {
            // free all previously allocated new nodes
            new_curr = new_list->head;
            for (int j = 0; j < i; ++j) {
                new_next = new_curr->next;
                (*free_func)(new_curr->data);
                free(new_curr);
                new_curr = new_next;
            }
            // free the new list
            free(new_list);
            return NULL;
        }

        // Set the node pointers
        new_curr->next = new_next;
        new_next->prev = new_curr;

        // Update the loop parameters
        new_curr = new_next;
        copy_curr = copy_curr->next;
    }

    // Set the head pointers
    new_curr->next = new_list->head;
    new_list->head->prev = new_curr;

    free(new_data);

    return new_list;
}


/**
 * Adds the given data at the given index
 * - if the index is negative, wrap around (ex: -1 becomes size - 1)
 * - if size + index < 0, keep wrapping around until a positive in bounds index is reached
 *   (ex: -5 with size 4 becomes index 4)
 * - if the index is greater than size - 1, wrap around (ex: size becomes 0)
 * - if index - size > size, keep wrapping around until a positive in bounds index is reached
 *   (ex: idx: 12, size:5 becomes idx:2)
 * - adding to an empty linked list
 *     - increment size
 *     - create a new node with the given data
 *     - set new node as head
 *     - set new nodes next and prev pointers to this node
 * - adding to the middle of the linked list
 *     - increment size
 *     - create a new node with the given data
 *     - traverse linked list to the (index - 1) node
 *     - insert new node between the (index - 1) node and (index) node
 * - if any memory allocation operations fail, do not change the linked list and return non-zero
 *     - the linked list should be in the same state it was in before add_to_linked_list was called
 * - if list is NULL, return non-zero
 * - data can be NULL
 *
 * @param list linked list that you're adding to
 * @param index you're adding data to
 * @param data you're putting into the linked list
 * @return 0 if add operation succeeded, non-zero otherwise
 */
int add_to_linked_list(circ_list *list, int index, void *data)
{
    if (list == NULL) {
        return 1;
    }

    // Wrap index around if negative
    if (index < 0) {
        while (index < 0) {
            index = list->size + index;
        }
    }
    while (list->size + index < 0) {
        index = list->size + index;
    }
    if (index > list->size) {
        index = index - list->size;
    }
    if (index - list->size > list->size) {
        while (index > list->size) {
            index = index - list->size;
        }
    }

    l_node* node_to_add = create_node(data);
    if (!node_to_add) {
        return 1;
    }

    if (list->size == 0) {
        list->size++;
        list->head = node_to_add;
        list->head->next = list->head;
        list->head->prev = list->head;
    } else if (index == 0) {
        list->size++;

        node_to_add->next = list->head;
        node_to_add->prev = list->head->prev;
        list->head = node_to_add;
    } else {
        list->size++;

        // Traverse to to the (index - 1)th node
        l_node* curr_node = NULL;
        curr_node = list->head;
        for (int i = 1; i < index; ++i) {
            curr_node = curr_node->next;
        }

        // Insert node_to_add between (index - 1)th (curr_node) and indexth node
        node_to_add->next = curr_node->next;
        node_to_add->prev = curr_node;
        curr_node->next = node_to_add;
        if (node_to_add->next) {
            node_to_add->next->prev = node_to_add;
        }
    }

    return 0;
}


/**
 * Gets the data pointer at the given index
 * - if the index is negative, wrap around (ex: -1 becomes size - 1)
 * - if size + index < 0, keep wrapping around until a positive in bounds index is reached
 *   (ex: -5 with size 4 becomes index 4)
 * - if the index is greater than size - 1, wrap around (ex: size becomes 0)
 * - if index - size > size, keep wrapping around until a positive in bounds index is reached
 *   (ex: idx: 12, size:5 becomes idx:2)
 * - if list or data_out are NULL, return non-zero
 *
 * @param list linked list you're getting data from
 * @param index you're getting data at
 * @param data_out pointer where you store data pointer removed from linked list
 * @return 0 if get operation succeeded, non-zero otherwise
 */
int get_from_linked_list(circ_list *list, int index, void** data_out)
{
    if (list == NULL || data_out == NULL) {
        return 1;
    }

    // Wrap index around if negative
    if (index < 0) {
        while (index < 0) {
            index = list->size + index;
        }
    }
    while (list->size + index < 0) {
        index = list->size + index;
    }
    if (index > list->size) {
        index = index - list->size;
    }
    if (index - list->size > list->size) {
        while (index > list->size) {
            index = index - list->size;
        }
    }

    // Traverse to the indexth node
    l_node* curr_node = list->head;
    for (int i = 1; i < index + 1; ++i) {
        curr_node = curr_node->next;
    }

    // Get the data from curr_node
    *data_out = curr_node->data;

    return 0;
}


/**
 * Removes the data at the given index and returns it to the user
 * - if the index is negative, wrap around (ex: -1 becomes size - 1)
 * - if size + index < 0, keep wrapping around until a positive in bounds index is reached
 *   (ex: -5 with size 4 becomes index 4)
 * - if the index is greater than size - 1, wrap around (ex: size becomes 0)
 * - if index - size > size, keep wrapping around until a positive in bounds index is reached
 *   (ex: idx: 12, size:5 becomes idx:2)
 * - removing from a size = 1 linked list
 *     - decrement size
 *     - save the data pointer
 *     - free the node
 *     - reset the head pointer to NULL
 * - removing from the middle of a linked list
 *     - decrement size
 *     - save the data pointer
 *     - remove this node from the linked list
 *         - set index+1's prev to index-1
 *         - set index-1's next to index+1
 *     - free the node
 * - removing from the head of the linked list (index 0)
 *     - same as "removing from the middle of a linked list"
 *     - head is changed to the node that used to be at index 1
 *     - if there was no node at index 1, set head to NULL
 * - if list or data_out are NULL, return non-zero
 *
 * @param list linked list you're removing from
 * @param index you're removing from linked list
 * @param data_out pointer where you store data pointer removed from linked list
 * @return 0 if remove operation succeeded, non-zero otherwise
 */
int remove_from_linked_list(circ_list *list, int index, void** data_out)
{
    if (list == NULL || data_out == NULL) {
        return 1;
    }

    // Wrap index around if negative
    if (index < 0) {
        while (index < 0) {
            index = list->size + index;
        }
    }
    while (list->size + index < 0) {
        index = list->size + index;
    }
    if (index > list->size) {
        index = index - list->size;
    }
    if (index - list->size > list->size) {
        while (index > list->size) {
            index = index - list->size;
        }
    }

    if (list->size == 1) { // Remove from size = 1 list
        list->size--;
        // Save the data being removed
        *data_out = list->head->data;

        // Free the node
        free(list->head);

        // Set head to NULL
        list->head = NULL;
    } else if (index == 0) { // Remove head
        list->size--;

        l_node* new_head = list->head->next;

        // Save the data being removed
        *data_out = list->head->data;

        // Remove old head
        if (new_head != NULL) {
            new_head->prev = list->head->prev;
            new_head->next = list->head->next;
        }

        // Free old head
        free(list->head);

        // Update the head
        list->head = new_head;
    } else { // Remove from middle
        list->size--;

        // Traverse to the indexth node
        l_node* curr_node = list->head;
        for (int i = 1; i < index + 1; ++i) {
            curr_node = curr_node->next;
        }

        // Save the data being removed
        *data_out = curr_node->data;

        // Remove curr_node
        if (curr_node->next) {
            curr_node->next->prev = curr_node->prev;
        }
        if (curr_node->prev) {
            curr_node->prev->next = curr_node->next;
        }

        // Free curr_node
        free(curr_node);
    }

    return 0;
}


/**
 * Checks if linked list contains data
 * - checks if each data pointer in each node equals the passed in data using eq_func
 * - terminate search on first match
 * - if data is found, return it through return_found
 * - if any parameter besides data is NULL, return false
 *
 * @param list linked list you're searching
 * @param data the data you're looking for
 * @param eq_func function used to check if two data pointers are equal (returns non-zero if equal)
 * @param return_found pointer through which you return found data if found
 * @return true if linked list contains data
 */
bool linked_list_contains(circ_list *list, void *data, list_eq eq_func, void **return_found)
{
    if (list == NULL || return_found == NULL) {
        return false;
    }

    l_node* curr_node = list->head;
    for (int i = 0; i < list->size; ++i) {
        if ((*eq_func)(curr_node->data, data)) {
            // Get the data from curr_node
            *return_found = curr_node->data;
            // Terminate search
            return true;
        }
        curr_node = curr_node->next;
    }

    return false;
}


/**
 * Zips two linked lists together
 * Example:
 *   a0 -> a1 -> a2 -> a3
 *   b0 -> b1 -> b2 -> b3
 * zip(a,b) becomes:
 *   a0 -> b0 -> a1 -> b1 -> a2 -> b2 -> a3 -> b3
 * - list_1 and list_2 should both be modified to point to the zipped list
 * - you can assume that list_1 and list_2 are equal in size
 * - if either parameter is NULL, return non-zero
 *
 * @param list_1 1st linked list you're zipping
 * @param list_2 2nd linked list you're zipping
 * @return 0 if zip operation succeeded, non-zero otherwise
 */
int zip(circ_list *list_1, circ_list *list_2)
{
    if (list_1 == NULL || list_2 == NULL) {
        return 1;
    }

    l_node* a = list_1->head;
    l_node* b = list_2->head;
    l_node* a_next = NULL;
    l_node* b_next = NULL;

    int new_size = list_1->size * 2;
    int i = 0;

    while (i < new_size) {
        // Save the next pointers
        a_next = a->next;
        b_next = b->next;

        // Zip the current nodes, making sure the prev and next pointers are correct
        a_next->prev = b;
        b->next = a_next;
        b->prev = a;
        a->next = b;

        // Update the loop parameters
        a = a_next;
        b = b_next;
        i += 2;
    }

    // Make both lists point to the same zipped list
    list_1->head = a;
    list_1->size = new_size;
    list_2->head = a;
    list_2->size = new_size;

    return 0;
}
