#include <stdio.h>
#include <stdlib.h>

#define QUEUESIZE 8
#define MODMASK 0x7
#define FALSE 0
#define TRUE 1

/* Function Prototypes */
void enqueue(int value);
int dequeue();
int queueFull();
int queueEmpty();
void display();

/* Global Variables */
int queue[QUEUESIZE];
int head = -1;
int tail = -1;

int main()
{
    int operation, value;
    do {
        system("clear");
        printf("### Queue Operations ###\n\n");
        printf("Press 1 - Enqueue, 2 - Dequeue, 3 - Display, 4 - Exit\n");
        printf("Your option? ");
        scanf("%d", &operation);
        switch (operation) {
            case 1:
                printf("\nEnter the positive integer value to be enqueued: ");
                scanf("%d", &value);
                enqueue(value);
                break;
            case 2:
                value = dequeue();
                if (value != -1)
                    printf("\nDequeued value is %d\n", value);
                break;
            case 3:
                display();
                break;
            case 4:
                printf("\nTerminating program\n");
                exit(0);
            default:
                printf("\nInvalid option! Try again.\n");
                break;
        }
        printf("\nPress the return key to continue . . . ");
        getchar();
        getchar();
    } while (operation != 4);
    return 0;
}

void enqueue(int value)
{
    if (queueFull()) {
        printf("\nQueue overflow! Cannot enqueue into a full queue.\n");
        return;
    }
    if (queueEmpty()) {
        head = tail = 0;
    } else {
        tail = ++tail & MODMASK;
    }
    queue[tail] = value;
}

int dequeue()
{
    register int value;
    if (queueEmpty()) {
        printf("\nQueue underflow! Cannot dequeue from an empty queue.\n");
        return (-1);
    }
    value = queue[head];
    if (head == tail) {
        head = tail = -1;
    } else {
        head = ++head & MODMASK;
    }
    return value;
}

int queueFull()
{
    if (((tail + 1) & MODMASK) == head)
        return TRUE;
    else
        return FALSE;
}

int queueEmpty()
{
    if (head == -1)
        return TRUE;
    else
        return FALSE;
}

void display()
{
    register int i, j, count;
    if (queueEmpty()) {
        printf("\nEmpty queue\n");
        return;
    }
    count = tail - head + 1;
    if (count <= 0)
        count += QUEUESIZE;
    printf("\nCurrent queue contents:\n");
    i = head;
    for (j = 0; j < count; j++) {
        printf(" %d", queue[i]);
        if (i == head) {
            printf(" <-- head of queue");
        }
        if (i == tail) {
            printf(" <-- tail of queue");
        }
        printf("\n");
        i = ++i & MODMASK;
    }
}
