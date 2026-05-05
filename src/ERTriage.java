import java.util.ArrayList;

public class ERTriage {

    private ArrayList<Patient> heap;

    public ERTriage() {
        this.heap = new ArrayList<>();
    }

    private int leftChild(int i) {
        return 2 * i;
    }

    private int rightChild(int i) {
        return 2 * i + 1;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    public void insert(Patient p) {
        heap.add(p);
        siftUp(heap.size() - 1);
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = parent(i);
            boolean shouldSwap = false;

            if (heap.get(i).severity < heap.get(p).severity) {
                shouldSwap = true;
            }

            if (shouldSwap) {
                Patient temp = heap.get(i);
                heap.set(i, heap.get(p));
                heap.set(p, temp);

            } else {
                break;
            }
        }
    }

    public Patient extractMax() {
        if (heap.isEmpty()) return null;
        return heap.remove(0);
    }

    private void siftDown(int i) {
        int left = leftChild(i);

        if (left < heap.size()) {
            if (heap.get(left).severity > heap.get(i).severity) {
                Patient temp = heap.get(i);
                heap.set(i, heap.get(left));
                heap.set(left, temp);

                siftDown(left);
            }
        }
    }
}