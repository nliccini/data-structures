/**
 * CS 2110 - Spring 2018 - Homework #10
 *
 * @author Patrick Tam + Sanjay Sood
 *
 * circ_list.h: DO NOT MODIFY
 */

#ifndef CIRC_LIST_H
#define CIRC_LIST_H

#include <stdbool.h>
#include "list.h"

struct _l_node;

typedef struct _circ_list {

    // Head pointer either points to a node with data or if the list is empty
    // NULL
    struct _l_node *head;

    // Size of the list
    int size;

} circ_list;

typedef struct _l_node
{
    void *data;
    struct _l_node *next;
    struct _l_node *prev;
} l_node;

/* Creating */
circ_list *create_linked_list(void);
circ_list *shallow_copy_linked_list(circ_list*);
circ_list *deep_copy_linked_list(circ_list*, list_copy, list_op);

/* Destroying */
int destroy_linked_list(circ_list* arr, list_op free_func);

/* Adding */
int add_to_linked_list(circ_list*, int, void*);

/* Querying */
int get_from_linked_list(circ_list *list, int index, void** data_out);
bool linked_list_contains(circ_list*, void*, list_eq, void**);

/* Removing */
int remove_from_linked_list(circ_list *list, int index, void** data_out);

/* Mutators */
int zip(circ_list *list_1, circ_list *list_2);

#endif /* CIRC_LIST_H */
