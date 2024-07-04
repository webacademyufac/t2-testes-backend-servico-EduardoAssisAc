package br.ufac.sgcmapi.model;

public class ConversorRomano {
    public int convert(String str) {
        char[] algarismos = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
        int [] inteiros = {1, 5, 10, 50, 100, 500, 1000};
        int n = 0, atual = -1, pos = -1, prox = -1;
        for (int i = 0; i < str.length(); i++) {
            try {
            for (int j = 0; j < algarismos.length; j++)
                if( str.charAt(i) == algarismos[j])
                    pos = j;
            atual = inteiros[pos];
            for (int j = 0; j < algarismos.length && i < str.length() - 1; j++)
                if( str.charAt(i) == algarismos[j])
                    pos = j;
            prox = inteiros[pos];
            }catch(ArrayIndexOutOfBoundsException e){
                return 0;
            }
            if (atual <= prox) 
                n += atual;
            else
                n -= atual;
            }
        return n;
    }
    
}
