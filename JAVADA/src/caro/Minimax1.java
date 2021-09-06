/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caro;

import static caro.co.bcwidth;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Admin-s
 */
public class Minimax1 {
    int[] mangdiemtc = {0,3,24,192,1536,12288,98304};
    int[] mangdiempn = {0,1,9,81,729,6561,59049};
    // Hàm kiểm tra xem ô nào có thể đi được
    public ArrayList<VT> cotheMoves(co c) {
        int[][] o = c.getO();
        ArrayList<VT> a = new ArrayList<VT>();
        for (int i = 0; i < co.bcheight; i++) {
            for (int j = 0; j < co.bcwidth; j++) {
                if (o[i][j] == 0) {
                    VT click = new VT();
                    click.setHeight(i);
                    click.setWidth(j);
                    a.add(click);
                }
            }
        }
        return a;
    }
    //hàm kiểm tra win
    public Boolean Find_Win(int[][] o, int player)
{
	for (int i = 0; i < co.bcheight; i++)
	{
		for (int j = 0; j < co.bcwidth; j++)
		{
			//xét hàng ngang
                        try{
			if (o[i][j] == player && o[i+1][j] == player && o[i+2][j] == player && o[i+3][j] == player && o[i+4][j] == player)
			{
				return true;
			}
                        }catch(Exception e){
                            
                        }
			//xét hàng dọc
                        try{
			if (o[i][j] == player && o[i][j+1] == player && o[i][j+2] == player && o[i][j+3] == player && o[i][j+4] == player)
			{
				return true;
			}
                        }catch(Exception e){
                            
                        }
			//chéo từ trên xuống qua bên phải
                        try{
			if (o[i][j] == player && o[i+1][j+1] == player && o[i+2][j+2] == player && o[i+3][j+3] == player && o[i+4][j+4] == player)
			{
				return true;
			}
                        }catch(Exception e){
                            
                        }
			//chéo từ trên xuống qua bên trái
                        try{
			if (o[i][j] == player && o[i-1][j+1] == player && o[i-2][j+2] == player && o[i-3][j+3] == player && o[i-4][j+4] == player)
			{
				return true;
			}
                        }catch(Exception e){
                            
                        }
		}
	}
	return false;
}
    //Hàm minimax

    public int[] minimax(co c,int player,int[] vt) {
        int DiemMax = 0;
        int DiemPhongNgu = 0;
        int DiemTanCong = 0;
        VT vtbest = new VT();
        //lưu lại bàn cờ
        int[][] oco = c.getO();
        if(player==2)
        {
            //lượt đi đầu tiên 
            if(!checktt(oco,player))
            {
                //nếu nước đi của người nằm viền 3 ô bên ngoài thì đánh ở giữa ngược lại đánh random xung quanh nước đi của người
                if(vt[0]>3 && vt[0] < co.bcheight-3)
                {
                    if(vt[1]>3 && vt[1] < co.bcwidth-3)
                    {
                        int[] ran = {-1,0,1};
                        Random r = new Random();
                        vtbest.setHeight(vt[0]+r.nextInt(ran.length)-1);
                        vtbest.setWidth(vt[1]+r.nextInt(ran.length)-1);
                    }
                }
                else 
                {
                    vtbest.setHeight(co.bcheight/2);
                    vtbest.setWidth(co.bcwidth/2);
                }
            } else
            {
                //tìm điểm cao nhất (Minimax)
                for (int i = 0; i < co.bcheight; i++) {
                    for (int j = 0; j < co.bcwidth; j++) {
                        
                        VT vitri = new VT();
                        vitri.setHeight(i);
                        vitri.setWidth(j);
                        //nếu nước cờ chưa có ai đánh và không bị cắt tỉa thì mới xét giá trị MinMax
                        if (oco[i][j] == 0 && !catTia(vitri,oco))
                            {
                                int DiemTam;
                                //nếu xung quanh nhiều quân ta thì điểm tấn công cao nếu nhiều quân địch điểm phòng ngự cao
                                DiemTanCong = duyetTC_Ngang(vitri,oco) + duyetTC_Doc(vitri,oco) + duyetTC_Sac(vitri,oco) + duyetTC_Huyen(vitri,oco);
                                DiemPhongNgu = duyetPN_Ngang(vitri,oco) + duyetPN_Doc(vitri,oco) + duyetPN_Sac(vitri,oco) + duyetPN_Huyen(vitri,oco);

                                if (DiemPhongNgu > DiemTanCong)
                                {
                                    DiemTam = DiemPhongNgu;
                                }
                                else
                                {
                                    DiemTam = DiemTanCong;
                                }
                                if (DiemMax < DiemTam)
                                {
                                    DiemMax = DiemTam;
                                    vtbest=vitri;
                                }
                            }
                    }
                }
            }
            
        }
        return new int[]{vtbest.getHeight(), vtbest.getWidth()};
    }
    //kiểm tra xem đã đánh ô nào chưa
    public boolean checktt(int[][] o,int p){
        for (int i = 0; i < co.bcheight; i++) {
            for (int j = 0; j < co.bcwidth; j++) {
                if(o[i][j]==p)
                    return true;
            }
        }
        return false;
    }
    //cho bàn cờ về như ban đầu
    public void resertma(int[][] o,int[][] ro){
        for (int j = 0; j < co.bcheight; j++) {
                    for (int k = 0; k < co.bcwidth; k++) {
                        o[j][k] = ro[j][k];
                    }
        }
    }
    // <editor-fold defaultstate="collapsed" desc=" Cắt tỉa Alpha betal ">
        Boolean catTia(VT oCo,int[][] o)
        {
            //nếu cả 4 hướng đều không có nước cờ thì cắt tỉa
            if (catTiaNgang(oCo,o) && catTiaDoc(oCo,o) && catTiaSac(oCo,o) && catTiaHuyen(oCo,o))
                return true;

            //chạy đến đây thì 1 trong 4 hướng vẫn có nước cờ thì không được cắt tỉa
            return false;
        }

        Boolean catTiaNgang(VT oCo,int[][] o)
        {
            //duyệt bên phải
            if (oCo.width <= co.bcwidth - 5)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height][oCo.width + i] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;

            //duyệt bên trái
            if (oCo.width >= 4)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height][oCo.width - i] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;
            //nếu chạy đến đây tức duyệt 2 bên đều không có nước đánh thì cắt tỉa
            return true;
        }
        Boolean catTiaDoc(VT oCo,int[][] o)
        {
            //duyệt phía giưới
            if (oCo.height <= co.bcheight - 5)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height + i][oCo.width] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;

            //duyệt phía trên
            if (oCo.height >= 4)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height - i][oCo.width] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;
            //nếu chạy đến đây tức duyệt 2 bên đều không có nước đánh thì cắt tỉa
            return true;
        }
        Boolean catTiaSac(VT oCo,int[][] o)
        {
            //duyệt từ trên xuống
            if (oCo.height <= co.bcheight - 5 && oCo.width >= 4)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height + i][oCo.width - i] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;

            //duyệt từ giưới lên
            if (oCo.width <= co.bcwidth - 5 && oCo.height >= 4)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height - i][oCo.width + i] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;

            //nếu chạy đến đây tức duyệt 2 bên đều không có nước đánh thì cắt tỉa
            return true;
        }
        Boolean catTiaHuyen(VT oCo,int[][] o)
        {
            //duyệt từ trên xuống
            if (oCo.height <= co.bcheight - 5 && oCo.width <= co.bcwidth - 5)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height + i][oCo.width + i] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;

            //duyệt từ giưới lên
            if (oCo.width >= 4 && oCo.height >= 4)
                for (int i = 1; i <= 4; i++)
                    if (o[oCo.height - i][oCo.width - i] != 0)//nếu có nước cờ thì không cắt tỉa
                        return false;

            //nếu chạy đến đây tức duyệt 2 bên đều không có nước đánh thì cắt tỉa
            return true;
        }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Điểm tấn công ">
        public int duyetTC_Ngang(VT oCo,int[][] o)
        {
            int DiemTanCong = 0;
            int SoQuanTa = 0;
            int SoQuanDichPhai = 0;
            int SoQuanDichTrai = 0;
            //đếm số khoản chống xung quanh có đủ để đánh thắng
            int KhoangChong = 0;
            //bên phải
            for (int dem = 1; dem <= 4 && oCo.width < co.bcwidth - 5; dem++)
            {
                //nếu là quân ta cộng thêm vô quân ta và khoảng chống
                if (o[oCo.height][oCo.width + dem] == 2)
                {
                    //nếu là ô kế bên cộng thêm 37 vào điểm tấn công
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    //nếu là quân địch thì cộng cho quân địch và ngược lại cộng cho khoảng chống
                    if (o[oCo.height][oCo.width + dem] == 1)
                    {
                        SoQuanDichPhai++;
                        break;
                    }
                    else KhoangChong++;
            }
            //bên trái
            for (int dem = 1; dem <= 4 && oCo.width > 4; dem++)
            {
                if (o[oCo.height][oCo.width - dem] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;

                }
                else
                    if (o[oCo.height][oCo.width - dem] == 1)
                    {
                        SoQuanDichTrai++;
                        break;
                    }
                    else KhoangChong++;
            }
            //khoảng chống không đủ tạo thành 5 nước thì không cộng điểm
            if (SoQuanDichPhai > 0 && SoQuanDichTrai > 0 && KhoangChong < 4)
                return 0;
            
            DiemTanCong -= mangdiempn[SoQuanDichPhai + SoQuanDichTrai];
            DiemTanCong += mangdiemtc[SoQuanTa];
            return DiemTanCong;
        }

        //duyệt dọc
        public int duyetTC_Doc(VT oCo,int[][] o)
        {
            int DiemTanCong = 0;
            int SoQuanTa = 0;
            int SoQuanDichTren = 0;
            int SoQuanDichDuoi = 0;
            int KhoangChong = 0;
            //bên trên
            for (int dem = 1; dem <= 4 && oCo.height > 4; dem++)
            {
                if (o[oCo.height - dem][oCo.width] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    if (o[oCo.height - dem][oCo.width] == 1)
                    {
                        SoQuanDichTren++;
                        break;
                    }
                    else KhoangChong++;
            }
            //bên dưới
            for (int dem = 1; dem <= 4 && oCo.height < co.bcheight - 5; dem++)
            {
                if (o[oCo.height + dem][oCo.width] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    if (o[oCo.height + dem][oCo.width] == 1)
                    {
                        SoQuanDichDuoi++;
                        break;
                    }
                    else KhoangChong++;
            }
            //bị chặn 2 đầu khoảng chống không đủ tạo thành 5 nước
            if (SoQuanDichTren > 0 && SoQuanDichDuoi > 0 && KhoangChong < 4)
                return 0;

            DiemTanCong -= mangdiempn[SoQuanDichTren + SoQuanDichDuoi];
            DiemTanCong += mangdiemtc[SoQuanTa];
            return DiemTanCong;
        }

        //sắc
        public int duyetTC_Sac(VT oCo,int[][] o)
        {
            int DiemTanCong = 1;
            int SoQuanTa = 0;
            int SoQuanDichCheoTren = 0;
            int SoQuanDichCheoDuoi = 0;
            int KhoangChong = 0;

            //sắc xuống
            for (int dem = 1; dem <= 4 && oCo.width < co.bcwidth - 5 && oCo.height < co.bcheight - 5; dem++)
            {
                if (o[oCo.height + dem][oCo.width + dem] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    if (o[oCo.height + dem][oCo.width + dem] == 1)
                    {
                        SoQuanDichCheoTren++;
                        break;
                    }
                    else KhoangChong++;
            }
            //sắc lên
            for (int dem = 1; dem <= 4 && oCo.height > 4 && oCo.width > 4; dem++)
            {
                if (o[oCo.height - dem][oCo.width - dem] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    if (o[oCo.height - dem][oCo.width - dem] == 1)
                    {
                        SoQuanDichCheoDuoi++;
                        break;
                    }
                    else KhoangChong++;
            }
            //bị chặn 2 đầu khoảng chống không đủ tạo thành 5 nước
            if (SoQuanDichCheoTren > 0 && SoQuanDichCheoDuoi > 0 && KhoangChong < 4)
                return 0;

            DiemTanCong -= mangdiempn[SoQuanDichCheoTren + SoQuanDichCheoDuoi];
            DiemTanCong += mangdiemtc[SoQuanTa];
            return DiemTanCong;
        }

        //huyền
        public int duyetTC_Huyen(VT oCo,int[][] o)
        {
            int DiemTanCong = 0;
            int SoQuanTa = 0;
            int SoQuanDichCheoTren = 0;
            int SoQuanDichCheoDuoi = 0;
            int KhoangChong = 0;

            //huyền lên
            for (int dem = 1; dem <= 4 && oCo.width < co.bcwidth - 5 && oCo.height > 4; dem++)
            {
                if (o[oCo.height - dem][oCo.width + dem] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    if (o[oCo.height - dem][oCo.width + dem] == 1)
                    {
                        SoQuanDichCheoTren++;
                        break;
                    }
                    else KhoangChong++;
            }
            //huyền xuống
            for (int dem = 1; dem <= 4 && oCo.width > 4 && oCo.height < co.bcheight - 5; dem++)
            {
                if (o[oCo.height + dem][oCo.width - dem] == 2)
                {
                    if (dem == 1)
                        DiemTanCong += 37;
                    SoQuanTa++;
                    KhoangChong++;
                }
                else
                    if (o[oCo.height + dem][oCo.width - dem] == 1)
                    {
                        SoQuanDichCheoDuoi++;
                        break;
                    }
                    else KhoangChong++;
            }
            //bị chặn 2 đầu khoảng chống không đủ tạo thành 5 nước
            if (SoQuanDichCheoTren > 0 && SoQuanDichCheoDuoi > 0 && KhoangChong < 4)
                return 0;

            DiemTanCong -= mangdiempn[SoQuanDichCheoTren + SoQuanDichCheoDuoi];
            DiemTanCong += mangdiemtc[SoQuanTa];
            return DiemTanCong;
        }
        // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Điểm phòng ngự ">
        public int duyetPN_Ngang(VT oCo,int[][] o)
        {
            int DiemPhongNgu = 0;
            int SoQuanTaTrai = 0;
            int SoQuanTaPhai = 0;
            int SoQuanDich = 0;
            int KhoangChongPhai = 0;
            int KhoangChongTrai = 0;
            Boolean ok = false;
            for (int dem = 1; dem <= 4 && oCo.width < co.bcwidth - 5; dem++)
            {
                
                if (o[oCo.height][oCo.width + dem] == 1)
                {
                    //nếu quân địch kế bên thì cộng thêm 9 điểm phòng ngự
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height][oCo.width + dem] == 2)
                    {
                        //nếu quân ta ở quá xa trừ 170
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaTrai++;
                        break;
                    }
                    else
                    { 
                        if (dem == 1)
                            ok = true;
                        KhoangChongPhai++;
                    }
            }
            //nếu quân địch có 3 đường liên tiếp mà chưa bị chặn - 200
            if (SoQuanDich == 3 && KhoangChongPhai == 1 && ok)
                DiemPhongNgu -= 200;
            ok = false;

            for (int dem = 1; dem <= 4 && oCo.width > 4; dem++)
            {
                if (o[oCo.height][oCo.width - dem] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height][oCo.width - dem] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaPhai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongTrai++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongTrai == 1 && ok)
                DiemPhongNgu -= 200;
            //nếu không đủ chỗ đánh thì trả về 0
            if (SoQuanTaPhai > 0 && SoQuanTaTrai > 0 && (KhoangChongTrai + KhoangChongPhai + SoQuanDich) < 4)
                return 0;
            DiemPhongNgu -= mangdiemtc[SoQuanTaTrai + SoQuanTaPhai];
            DiemPhongNgu += mangdiempn[SoQuanDich];
            return DiemPhongNgu;
        }
        public int duyetPN_Doc(VT oCo,int[][] o)
        {
            int DiemPhongNgu = 0;
            int SoQuanTaTrai = 0;
            int SoQuanTaPhai = 0;
            int SoQuanDich = 0;
            int KhoangChongTren = 0;
            int KhoangChongDuoi = 0;
            Boolean ok = false;

            //lên
            for (int dem = 1; dem <= 4 && oCo.height > 4; dem++)
            {
                if (o[oCo.height - dem][oCo.width ] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;

                }
                else
                    if (o[oCo.height - dem][oCo.width ] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaPhai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongTren++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongTren == 1 && ok)
                DiemPhongNgu -= 200;
            ok = false;
            //xuống
            for (int dem = 1; dem <= 4 && oCo.height < co.bcheight - 5; dem++)
            {
                //gặp quân địch
                if (o[oCo.height + dem][oCo.width ] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height + dem][oCo.width ] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaTrai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongDuoi++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongDuoi == 1 && ok)
                DiemPhongNgu -= 200;
            if (SoQuanTaPhai > 0 && SoQuanTaTrai > 0 && (KhoangChongTren + KhoangChongDuoi + SoQuanDich) < 4)
                return 0;
            DiemPhongNgu -= mangdiemtc[SoQuanTaTrai + SoQuanTaPhai];
            DiemPhongNgu += mangdiempn[SoQuanDich];
            return DiemPhongNgu;
        }
        public int duyetPN_Sac(VT oCo,int[][] o)
        {
            int DiemPhongNgu = 0;
            int SoQuanTaTrai = 0;
            int SoQuanTaPhai = 0;
            int SoQuanDich = 0;
            int KhoangChongTren = 0;
            int KhoangChongDuoi = 0;
            Boolean ok = false;
            //lên
            for (int dem = 1; dem <= 4 && oCo.height < co.bcheight - 5 && oCo.width < co.bcwidth - 5; dem++)
            {
                if (o[oCo.height + dem][oCo.width + dem] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height + dem][oCo.width + dem] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaPhai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongTren++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongTren == 1 && ok)
                DiemPhongNgu -= 200;
            ok = false;
            //xuống
            for (int dem = 1; dem <= 4 && oCo.height > 4 && oCo.width > 4; dem++)
            {
                if (o[oCo.height - dem][oCo.width - dem] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height - dem][oCo.width - dem] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaTrai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongDuoi++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongDuoi == 1 && ok)
                DiemPhongNgu -= 200;
            if (SoQuanTaPhai > 0 && SoQuanTaTrai > 0 && (KhoangChongTren + KhoangChongDuoi + SoQuanDich) < 4)
                return 0;
            DiemPhongNgu -= mangdiemtc[SoQuanTaPhai + SoQuanTaTrai];
            DiemPhongNgu += mangdiempn[SoQuanDich];
            return DiemPhongNgu;
        }
        public int duyetPN_Huyen(VT oCo,int[][] o)
        {
            int DiemPhongNgu = 0;
            int SoQuanTaTrai = 0;
            int SoQuanTaPhai = 0;
            int SoQuanDich = 0;
            int KhoangChongTren = 0;
            int KhoangChongDuoi = 0;
            Boolean ok = false;
            //lên
            for (int dem = 1; dem <= 4 && oCo.height > 4 && oCo.width < co.bcwidth - 5; dem++)
            {
                if (o[oCo.height - dem][oCo.width + dem] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height - dem][oCo.width + dem] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaPhai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongTren++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongTren == 1 && ok)
                DiemPhongNgu -= 200;
            ok = false;
            //xuống
            for (int dem = 1; dem <= 4 && oCo.height < co.bcheight - 5 && oCo.width > 4; dem++)
            {
                if (o[oCo.height + dem][oCo.width - dem] == 1)
                {
                    if (dem == 1)
                        DiemPhongNgu += 9;
                    SoQuanDich++;
                }
                else
                    if (o[oCo.height + dem][oCo.width - dem] == 2)
                    {
                        if (dem == 4)
                            DiemPhongNgu -= 170;
                        SoQuanTaTrai++;
                        break;
                    }
                    else
                    {
                        if (dem == 1)
                            ok = true;
                        KhoangChongDuoi++;
                    }
            }
            if (SoQuanDich == 3 && KhoangChongDuoi == 1 && ok)
                DiemPhongNgu -= 200;
            if (SoQuanTaPhai > 0 && SoQuanTaTrai > 0 && (KhoangChongTren + KhoangChongDuoi + SoQuanDich) < 4)
                return 0;
            DiemPhongNgu -= mangdiemtc[SoQuanTaTrai + SoQuanTaPhai];
            DiemPhongNgu += mangdiempn[SoQuanDich];
            return DiemPhongNgu;
        }
    // </editor-fold>
}
