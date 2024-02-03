import java.util.ArrayList;

public class Combinacao {

    private int id;
    private ArrayList<String> jogados = new ArrayList<>();
    private ArrayList<String> acertos = new ArrayList<>();
    private int quantidadeAcertos = acertos.size();

    public Combinacao(int id){
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getJogado() {
        return jogados;
    }

    public ArrayList<String> getAcertos() {
        return acertos;
    }

    public int getQuantidadeAcertos() {
        return quantidadeAcertos;
    }

    public void setQuantidadeAcertos(int quantidadeAcertos) {
        this.quantidadeAcertos = quantidadeAcertos;
    }

    @Override
    public String toString() {
        String printJogados = "";
        String printAcertos = "";
        for(String x: jogados){
            printJogados += x + " | " + "\n";
        }
        for(String x: acertos){
            printAcertos += x;
        }
        return "id: " + id + "\n"
                + "jogados: " + printJogados + "\n"
                + "acertos: " + printAcertos + "\n"
                + "quantidade de acertos: " + acertos.size() + "\n";
    }
}
