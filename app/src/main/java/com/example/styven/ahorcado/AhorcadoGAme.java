package com.example.styven.ahorcado;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

/**
 * Created by Pelusa on 24/09/2016.
 */
public class AhorcadoGAme {
    private FirebaseDatabase firebaseDatabase;
    private static String palabra;
    private static String TAG = "AhorcadoGame";
    private String palabraSecreta;
    private DatabaseReference myRef;
    private int errores;
    private static boolean gano;
    private static boolean perdio;
    // si esLetra es true, es una letra, de lo contrario es un intento de adivinar la palabra
    private boolean esLetra;
    private boolean correcta;
    private String[] arraySecreto;

    public AhorcadoGAme(String word){
        palabra = word.trim();
        errores = 0;
        gano = false;
        perdio = false;
        esLetra = false;
        arraySecreto = new String[palabra.length()];
        palabraSecreta = formarPalabraSecreta(word);

    }

    private String formarPalabraSecreta(String word){
        Log.v(TAG, "Palabra aleatoria: " + word);
        String temPalabra = "";

        for(int i=0; i < word.length() ; i++){
            arraySecreto[i] = "_";
            if(i != (palabra.length() - 1)){
                temPalabra += "_ ";
            }else{
                temPalabra += "_";

            }
        }
        return temPalabra;
    }

    private void reformarPalabraSecreta(String[] array){
        palabraSecreta = "";
        for(int i = 0; i < array.length ; i++){
            if(i != (array.length - 1)){
                palabraSecreta += array[i]+" ";
            }else{
                palabraSecreta += array[i];
            }
        }
    }

    public String getPalabra() {
        return palabra;
    }

    public String getPalabraSecreta(){
        return palabraSecreta;
    }

    public boolean getPerdio() { return perdio; }

    public boolean getGano() {  return gano; }

    public boolean getEsLetra() {  return esLetra; }

    public boolean esCorrecta() {  return correcta; }

    public int getErrores() {  return errores; }

    public void validar(String wordLetter){

        if (wordLetter.trim().length() > 1){
            // *************  es una palabra

            esLetra = false;
            Log.v(TAG, "Palabra: Es una palabra lo que ingresaste " + wordLetter);
            String temPalabra = wordLetter.toString();

            if(wordLetter.toLowerCase().trim().equals(palabra.toLowerCase().trim())){
                Log.v(TAG, "el ingreso y la palabra son iguales");
                gano = true;
            }

        }else{
            // **************** es una letra

            esLetra = true;
            Log.v(TAG, "Palabra: Es una letra lo que ingresaste " + wordLetter);

            if(palabra.contains(wordLetter)){
                perdio = false;
                correcta = true;
                // aqui se modifica palabra secreta para reimprimirla
                for (int i=1;i <= palabra.length();i++){
                    if(palabra.substring((i-1),i).equals(wordLetter)){
                       arraySecreto[i-1] = wordLetter;
                    }
                }
                Log.v(TAG, "Array Palabra secreta = " + Arrays.toString(arraySecreto));
                reformarPalabraSecreta(arraySecreto);
            }else{
                correcta = false;
                errores ++;
                if (errores >= 5){
                    perdio = true;
                }
            }
        }
    }

}
