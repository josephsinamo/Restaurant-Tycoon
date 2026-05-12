/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author WINDOWS
 */
public class ujiCoba {

    

    public static void main(String[] args) {
        resto r = new resto();
        pelanggan p = new pelanggan();
        
        r.layani(p);
    }
}

class resto{
    int [] lol = new int [3];
    
    public void layani(pelanggan p){
        p.pesanan(lol);
    }
}

class pelanggan{
    public void pesanan(int [] l){
        
    }
    
}
