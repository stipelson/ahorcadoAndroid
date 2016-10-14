package com.example.styven.ahorcado;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pelusa on 11/10/2016.
 */
public class Game {
    public String uid;
    public String tiempo;
    public boolean gano;
    public String palabra;
    public int errores;
    

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Game(String uid, String palabra, String tiempo, int errores, boolean gano) {
        this.uid = uid;
        this.tiempo = tiempo;
        this.palabra = palabra;
        this.gano = gano;
        this.errores = errores;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("palabra", palabra);
        result.put("tiempo", tiempo);
        result.put("errores", errores);
        result.put("gano", gano);

        return result;
    }

}
