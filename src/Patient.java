public class Patient {
    public String id;
    public int severity;

    public Patient(String id, int severity) {
        this.id = id;
        this.severity = severity;
    }

    public String toString(){
        return "Patient{" + id + ", " + severity + "}";
    }
}