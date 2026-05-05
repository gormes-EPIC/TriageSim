public class Main{

    public static void main(String[] args){
        ERTriage ptmc = new ERTriage();

        ptmc.insert(new Patient("Alice", 5));
        ptmc.insert(new Patient("Bob", 2));
        ptmc.insert(new Patient("Charlie", 8));

        System.out.println(ptmc.extractMax());
    }
}
