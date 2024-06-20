
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#define N 5  // Number of chairs in the waiting room

sem_t mutex;
sem_t customers;
sem_t barber;
int waiting_count = 0;

void* customer(void* id) {
    int customer_id = *((int*)id);
    free(id);

    sem_wait(&mutex);
    if (waiting_count == N) {
        sem_post(&mutex);
        printf("Customer %d leaving, no chairs available.\n", customer_id);
        return NULL;
    }
    waiting_count++;
    printf("Customer %d sitting in waiting room. Waiting count: %d\n", customer_id, waiting_count);
    sem_post(&customers);
    sem_post(&mutex);

    sem_wait(&barber);
    printf("Customer %d getting a haircut.\n", customer_id);
    // Simulate getting a haircut
    sleep(1);
    printf("Customer %d finished getting a haircut.\n", customer_id);
    return NULL;
}

void* barber_thread(void* arg) {
    while (1) {
        sem_wait(&customers);
        sem_wait(&mutex);
        waiting_count--;
        printf("Barber ready for a customer. Waiting count: %d\n", waiting_count);
        sem_post(&barber);
        sem_post(&mutex);

        // Simulate cutting hair
        printf("Barber is cutting hair.\n");
        sleep(1);
        printf("Barber finished cutting hair.\n");
    }
    return NULL;
}

int main() {
    pthread_t barber_tid;
    pthread_t customer_tid[10];

    sem_init(&mutex, 0, 1);
    sem_init(&customers, 0, 0);
    sem_init(&barber, 0, 0);

    pthread_create(&barber_tid, NULL, barber_thread, NULL);

    // Create customer threads
    for (int i = 0; i < 10; i++) {
        int* id = malloc(sizeof(int));
        *id = i + 1;
        pthread_create(&customer_tid[i], NULL, customer, id);
        // Simulate customers arriving at different times
        sleep(rand() % 3);
    }

    // Join customer threads
    for (int i = 0; i < 10; i++) {
        pthread_join(customer_tid[i], NULL);
    }

    // Since the barber thread is an infinite loop, we don't join it here.
    // In a real application, you would have a way to gracefully shut down the barber thread.

    sem_destroy(&mutex);
    sem_destroy(&customers);
    sem_destroy(&barber);

    return 0;
}
