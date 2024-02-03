import front.DataCallback;
import front.MyFrame;
import front.MyGUI;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Program {

    public static void main(String[] args) {
        //Caminho para o database
        /*
        String pathTo15thousand = "src/bancoDeDados/LINHAS15MIL";
        String pathTo20lines = "src/bancoDeDados/20DEZENAS";
        String pathToStoreResult = "src/bancoDeDados/RESULTADOS";

         */

        CountDownLatch latch = new CountDownLatch(2);

        String pathTo15thousand = retornarData("Insira as 15 mil linhas", latch);
        String pathTo20lines = retornarData("Insira as dezenas de 20", latch);

        // Wait until all frames are interacted with
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //parte para pegar os dados do usuário
        String[] jogadoArray = lerString(pathTo20lines).split(" ");
        String[] jogadoArrayEmLinha = lerStringEmLinha(pathTo20lines).split("-");
        String[] paraTestar = lerString(pathTo15thousand).split(" ");
        int tamanhoDoGrupo = 5;


        //inicializar o array com as combinações
        Combinacao[] combinacoes = new Combinacao[jogadoArrayEmLinha.length/tamanhoDoGrupo];
        for (int i = 0; i < combinacoes.length; i++) {
            combinacoes[i] = new Combinacao(i);
        }

        //adicionao às combinações suas jogadas.
        int countArray = 0;
        for (int i = 0; i < combinacoes.length; i++) {
            for (int j = 0; j < tamanhoDoGrupo; j++) {
                combinacoes[i].getJogado().add(jogadoArrayEmLinha[countArray]);
                countArray++;
            }
        }

        //combinar tudo de uma só vez

        //parte para definir a matriz para inserir os dados para fazer o teste(grupo de 15)
        String matrizParaTestar[][] = popularMatriz(paraTestar, new String[paraTestar.length/15][15]);


        //parte para definir a matriz das dezenas de 20 para testar nas de 15
        String matrizJogada[][] = popularMatriz(jogadoArray, new String[jogadoArray.length/20][20]);

        String tempResposta = "";
        fazerCombinacoes(combinacoes, matrizParaTestar);
        for(Combinacao x: combinacoes){
            System.out.println(x);
        }
        for(Combinacao x: combinacoes){
            tempResposta+= x + "\n";
        }
        final String resposta = tempResposta;
        SwingUtilities.invokeLater(() -> new MyFrame(resposta));
        //criarEEscreverArquivo(pathToStoreResult, combinacoes);





    }

    public static void fazerCombinacoes(Combinacao[] combinacoes, String[][]matrizParaTestar){

        for (int i = 0; i < combinacoes.length; i++) {
            fazerCombinacao(combinacoes[i], 0, matrizParaTestar);
            String[] array;

            for (int j = 1; j < combinacoes[i].getJogado().size(); j++) {
                array = popularString(listToArray(combinacoes[i].getAcertos()));
                fazerCombinacao(combinacoes[i], j, popularMatriz(array,new String[combinacoes[i].getAcertos().size()][15]));
            }
        }



    }


    public static void fazerCombinacoes(Combinacao combinacao, String[][]matrizParaTestar){

        fazerCombinacao(combinacao, 0, matrizParaTestar);
        String[] array;


        for (int i = 1; i < combinacao.getJogado().size(); i++) {
            array = popularString(listToArray(combinacao.getAcertos()));
            fazerCombinacao(combinacao, i, popularMatriz(array,new String[combinacao.getAcertos().size()][15]));
        }


    }

    public static void fazerCombinacao(Combinacao combinacao, int posicao, String[][]matrizParaTestar) {
        combinacao.getAcertos().clear();
        int acertados = 0;
        String[] jogadoArray = combinacao.getJogado().get(posicao).split(" ");

        //Loop nested que vai passar por cada linha do matrizParaTestar e comparar as dezenas iguais.
        for (int countA = 0; countA < matrizParaTestar.length; countA++) {//itera a linha do matrizParaTestar
            acertados = 0; //reseta o contador para contar a próxima linha, pois a linha passada não teve acertos suficientes
            for (int countB = 0; countB < jogadoArray.length; countB++) {//itera 22 vezes comparando
                for (int countC = 0; countC < matrizParaTestar[0].length; countC++) { //itera 15 vezes comparando
                    if (jogadoArray[countB].equals(matrizParaTestar[countA][countC])) {
                        acertados++;
                        break;
                    }
                }
                if (acertados == 15) {
                    combinacao.getAcertos().add(Arrays.toString(matrizParaTestar[countA])
                            .replace("[", "").replace("]", "")
                            .replace(",", ""));
                    acertados = 0;
                }
            }
        }
        //System.out.println("quantidade de acertos: " + combinacao.getAcertos().size());
    }


    public static String[] listToArray(ArrayList<String> array){
        String[] arrayResult = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            arrayResult[i] = array.get(i);
        }
        return arrayResult;
    }


    //transformar [xx, xx, xx, xx] em [xx], [xx],

    public static String[][] popularMatriz(String[] array, String[][] matriz) {
        int linhas = matriz.length;
        int colunas = matriz[0].length; // Assumindo que todas as linhas têm o mesmo comprimento
        int arrayIndex = 0;

        for (int countA = 0; countA < linhas; countA++) {
            for (int countB = 0; countB < colunas; countB++) {
                matriz[countA][countB] = array[arrayIndex];
                arrayIndex++;
            }
        }
        return matriz;
    }

    public static String[] popularString(String[] bigArray){
        String[] resultArray = new String[bigArray.length * 20];

        int count = 0;
        for (int i = 0; i < bigArray.length; i++) {
            for (int j = 0; j < bigArray[0].split(" ").length; j++) {
                resultArray[count] = bigArray[i].split(" ")[j];
                count++;
            }
        }
        return resultArray;
    }

    public static String[] popularString(String[] array, String[][] matriz){
        int arrayCount = 0;
        for (int i = 0; i < matriz.length; i++) {
            array[i] = "";
            for (int j = 0; j < matriz[0].length; j++) {
                array[i] += matriz[i][arrayCount] + " ";
                arrayCount++;
            }
            arrayCount = 0;
        }
        return array;
    }

    public static String lerDocumento(String path){
        String jogados = "";
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {
                // faz alguma coisa
                jogados = jogados + line + " ";
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jogados;
    }

    public static String lerString(String text){
        String jogados = "";
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            // Read each line from the BufferedReader
            String line = reader.readLine();
            while (line != null) {
                jogados = jogados + line + " ";
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jogados;
    }

    public static String lerDocumentoEmLinha(String path){
        String jogados = "";
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {
                // faz alguma coisa
                jogados = jogados + line + "-";
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jogados;
    }

    //try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
    public static String lerStringEmLinha(String text){
        String jogados = "";

        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line = reader.readLine();

            while (line != null) {
                // faz alguma coisa
                jogados = jogados + line + "-";
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jogados;
    }

    public static void popularDocumento(String path, Combinacao[] combinacoes){
        try{
            PrintWriter writer = new PrintWriter(path);

            for (int count = 0; count < combinacoes.length; count++) {
                writer.println(combinacoes[count]);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printarCombinacoes(Combinacao[] combinacoes){
        for (int i = 0; i < combinacoes.length; i++) {
            System.out.println(combinacoes[i]);
        }
    }

    //criar e escrever em um novo arquivo

    public static void criarEEscreverArquivo(String path, Combinacao[] combinacoes){
        //usar o isFile para checar se tal arquivo existe
        //criar um arquivo novo
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //escrever no novo arquivo as informações do primeiro

        popularDocumento(path, combinacoes);
    }

    public static String retornarData(String title, CountDownLatch latch) {
        // Create a BlockingQueue to store the received data
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        MyGUI myGUI = new MyGUI(title, latch, new DataCallback() {
            @Override
            public void onDataReceived(String data) {
                try {
                    // Put the received data into the queue
                    queue.put(data);
                    // Count down the latch
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Retrieve and return the data from the queue
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }



    /*
    public static Combinacao fazerCombinacao(String[] jogadoArray, String[][]matrizParaTestar){
        int acertados = 0;
        ArrayList<String> jogadasCorretas = new ArrayList<>();
        //Loop nested que vai passar por cada linha do matrizParaTestar e comparar as dezenas iguais.
        for(int countA = 0; countA < matrizParaTestar.length; countA++){//itera a linha do matrizParaTestar
            acertados = 0; //reseta o contador para contar a próxima linha, pois a linha passada não teve acertos suficientes
            for(int countB = 0; countB < jogadoArray.length; countB++){//itera 22 vezes comparando
                for(int countC = 0; countC < matrizParaTestar[0].length; countC++){ //itera 15 vezes comparando
                    if(jogadoArray[countB].equals(matrizParaTestar[countA][countC]) ){
                        acertados++;
                        break;
                    }
                }
                if(acertados == 15){
                    jogadasCorretas.add(Arrays.toString(matrizParaTestar[countA]));
                    acertados = 0;
                }
            }
        }
        Combinacao novaCombinacao = new Combinacao(Arrays.toString(jogadoArray), jogadasCorretas, jogadasCorretas.size());
        return novaCombinacao;
    }

     */

    /*
    public static ArrayList<Combinacao> fazerCombinacoes(String[][] matrizJogada, String[][]matrizParaTestar){
        ArrayList<Combinacao> combinacoes = new ArrayList<>();
        for(int count = 0; count < matrizJogada.length; count++){
            Combinacao novaCombinacao = fazerCombinacao(matrizJogada[count], matrizParaTestar);
            combinacoes.add(novaCombinacao);
        }
        return combinacoes;
    }

     */


}