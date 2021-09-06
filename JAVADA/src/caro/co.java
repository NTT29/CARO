/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caro;

/**
 *
 * @author Admin-s
 */
public class co {
    //biến xđ win hay chưa
    public int fn =0;
    //biến lưu player win
    public int fn_win=0;
    //kích thước 1 ô cờ
    public static int oc = 30;
    //số ô ngang
    public static int bcwidth = 18;
    //số ô dọc
    public static int bcheight = 18;
    //kích thước của bàn cờ
    public static int height = oc*bcheight+1;
    public static int width = oc*bcwidth+1;
    //bước nhảy mỗi lần
    public static int cooldown_step = 100;
    //thời gian chờ tối đa
    public static int cooldown_time = 10000;
    //thời gian delay
    public static int cooldown_in = 100;
    //bàn cờ
    int[][] o;
    //khởi tạo
    public co(){
            o = new int[bcheight][bcwidth];
            for(int i=0;i<bcheight;i++)
            {
                for(int j=0;j<bcwidth;j++)
                    {
                        o[i][j]=0;
                    }
            }
    }
    //in bàn cờ hiện tại
    public void s(){
            for(int i=0;i<bcheight;i++)
            {
                for(int j=0;j<bcwidth;j++)
                    {
                        System.out.print("  "+o[i][j]);
                    }
                System.out.print("\n");
            }
    }

    public int[][] getO() {
        return o;
    }

    public void setO(int[] o,int p) {
            this.o[o[0]][o[1]] = p;
    }
    //kiểm tra win
    public boolean checkwin(int[] o,int p){
        if(winng(o,p) == true || windc(o,p) == true || winhn(o,p) == true || winsc(o,p)== true )
        {
            fn=1;
            fn_win=p;
        }
        return winng(o,p) || windc(o,p) || winhn(o,p) || winsc(o,p);
    }
    //win ngang
    public boolean winng(int[] o,int p){
        int countL=0;
        int countR=0;
        for (int i = o[1]; i < bcwidth; i++) {
            if(this.o[o[0]][i]==p)
                countR++;
            else break;
        }
        for (int i = o[1]-1; i >= 0; i--) {
            if(this.o[o[0]][i]==p)
                countL++;
            else break;
        }
        if(countL + countR ==5)
            return true;
        return false;
    }
    //win dọc
    public boolean windc(int[] o,int p){
        int countT=0;
        int countB=0;
        for (int i = o[0]; i < bcheight; i++) {
            if(this.o[i][o[1]]==p)
                countT++;
            else break;
        }
        for (int i = o[0]-1; i >= 0; i--) {
            if(this.o[i][o[1]]==p)
                countB++;
            else break;
        }
        if(countT + countB ==5)
            return true;
        return false;
    }
    //win chéo huyền
    public boolean winhn(int[] o,int p){
        int countT=0;
        int countB=0;
        for (int i = 0; i <= o[0] && i <= o[1]; i++) {
            if(o[0]-i < 0 || o[1]-i < 0)
                break; 
            if(this.o[o[0]-i][o[1]-i]==p)
                countT++;
            else break;
        }
        for (int i = 1; i < bcheight - o[0] && i < bcwidth - o[1]; i++) {
            if(o[0]+i >= bcwidth || o[1]+i > bcwidth)
                break; 
            if(this.o[o[0]+i][o[1]+i]==p)
                countB++;
            else break;
        }
        if(countT + countB ==5)
            return true;
        return false;
    }
    //win sắc
    public boolean winsc(int[] o,int p){
        int countT=0;
        int countB=0;
        for (int i = 0; i <= o[0] && i <= bcwidth - o[1]; i++) {
            if(o[0]-i < 0 || o[1]+i >= bcwidth)
                break; 
            if(this.o[o[0]-i][o[1]+i]==p)
                countT++;
            else break;
        }
        for (int i = 1; i < bcheight - o[0] && i < o[1] + 1; i++) {
            if(o[0]+i >= bcheight || o[1]-i < 0)
                break; 
            if(this.o[o[0]+i][o[1]-i]==p)
                countB++;
            else break;
        }
        if(countT + countB ==5)
            return true;
        return false;
    }
}
