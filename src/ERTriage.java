import java.util.ArrayList;

public class SabotagedERTriage {

    private Patient[] heap;
    private int activeSize;

    public SabotagedERTriage(int capacity) {
        this.heap = new Patient[capacity];
        this.activeSize = 0; // Needed for Sabotage #10
    }

    // SABOTAGE #1: The "Zero-Index" Mix-Up
    // Left child of a 0-indexed array should be (2 * i) + 1. 
    private int leftChild(int i) {
        return 2 * i;
    }

    private int rightChild(int i) {
        return 2 * i + 1; // Should be (2 * i) + 2
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    public void insert(Patient p) {
        if (activeSize == heap.length) return; // Prevent basic overflow
        heap[activeSize] = p;
        siftUp(activeSize);
        activeSize++;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = parent(i);
            boolean shouldSwap = false;

            // SABOTAGE #2: The "Upside-Down ER"
            // We check if the current patient is LESS severe than the parent. (Creates a Min-Heap)
            if (heap[i].severity < heap[p].severity) {
                shouldSwap = true;
            }
            else if (heap[i].severity == heap[p].severity) {
                // SABOTAGE #9: The "Time Paradox" Stability Bug
                // Max-heaps prioritize the largest values. 
                // A larger timestamp means they arrived LATER. 
                // This tie-breaker treats the NEWEST patients first!
                if (heap[i].arrivalTimestamp > heap[p].arrivalTimestamp) {
                    shouldSwap = true;
                }
                else if (heap[i].arrivalTimestamp == heap[p].arrivalTimestamp) {
                    // SABOTAGE #7 (Continued): The "Tie-Breaker" TypeError
                    // If severity and timestamps tie, we attempt a raw object comparison.
                    // Because Patient doesn't implement Comparable, this throws a ClassCastException.
                    @SuppressWarnings("unchecked")
                    Comparable<Patient> c = (Comparable<Patient>) heap[i];
                    c.compareTo(heap[p]);
                }
            }

            if (shouldSwap) {
                Patient temp = heap[i];
                heap[i] = heap[p];
                heap[p] = temp;

                // SABOTAGE #5: The "Infinite Waiting Room"
                // We forget to update the index! The loop will infinitely swap the same two elements.
                // i = p;  <-- THIS IS DELIBERATELY MISSING
            } else {
                break;
            }
        }
    }

    public Patient extractMax() {
        if (activeSize == 0) return null;

        Patient maxPatient = heap[0];

        // SABOTAGE #6: The "Lone Survivor" Crash
        // If activeSize was 1, we decrement it to 0. 
        // Then we assign heap[0] = heap[0], which does nothing.
        // Then siftDown(0) runs, checks leftChild(0) -> 0. 
        // Array math gets tangled and throws an OutOfBounds or loops weirdly depending on siftDown logic.
        activeSize--;
        heap[0] = heap[activeSize];

        // SABOTAGE #10: The "Ghost Patient" Memory Leak
        // We never clear the pointer at the end of the array!
        // heap[activeSize] = null; <-- THIS IS DELIBERATELY MISSING
        // The patient is "extracted" but their data is still lingering in memory.

        siftDown(0);
        return maxPatient;
    }

    // SABOTAGE #3: The "Lazy Shift" Deletion
    // If your friend converts the array to an ArrayList, this is the trap they fall into.
    // It destroys the tree structure and runs in O(N) time.
    public Patient badExtractMax(ArrayList<Patient> listHeap) {
        if (listHeap.isEmpty()) return null;
        return listHeap.remove(0);
    }

    private void siftDown(int i) {
        int left = leftChild(i);

        // SABOTAGE #4: The "Left-Leaning Doctor"
        // We completely forget to calculate or check the rightChild! 
        // If the right child has a severity of 10, and the left has a 2, we promote the 2.
        if (left < activeSize) {
            if (heap[left].severity > heap[i].severity) {
                Patient temp = heap[i];
                heap[i] = heap[left];
                heap[left] = temp;
                siftDown(left);
            }
        }
    }

    // SABOTAGE #8: The "Silent Deterioration" (State Mutation)
    public void updatePatientSeverity(String patientId, int newSeverity) {
        for (int i = 0; i < activeSize; i++) {
            if (heap[i].id.equals(patientId)) {
                heap[i].severity = newSeverity;
                // We mutate the state but NEVER trigger a re-heapify!
                // siftUp(i);   <-- DELIBERATELY MISSING
                // siftDown(i); <-- DELIBERATELY MISSING
                break;
            }
        }
    }

    // Helper method that exposes Sabotage #10
    public void printAllPatientsInSystem() {
        // A naive developer loops over the physical array length instead of the activeSize.
        // Because of Sabotage #10, this prints patients who were already discharged!
        for (int i = 0; i < heap.length; i++) {
            if (heap[i] != null) {
                System.out.println("Patient " + heap[i].id + " is in the ER.");
            }
        }
    }
}