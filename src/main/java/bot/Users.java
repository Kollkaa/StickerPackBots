package bot;

import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.ArrayList;

public class Users {



    private String name;
    private Long chatid;
    private ArrayList<Sticker> stickers;
    private  int zakaz=0;

    public Users(Long chatid, ArrayList<Sticker> stickers,String name) {
        this.chatid = chatid;
        this.name=name;
        this.stickers = stickers;
    }

    public String getName() {
        return name;
    }

    public  int getZakaz() {
        return zakaz;
    }

    public void setZakaz() {
        this.zakaz+=1;
    }



    public Long getChatid() {
        return chatid;
    }

    public void setChatid(Long chatid) {
        this.chatid = chatid;
    }

    public ArrayList<Sticker> getStickers() {
        return stickers;
    }
    public void AddSticker(Sticker sticker)
    {
        stickers.add(sticker);
    }

    public void setStickers(ArrayList<Sticker> stickers) {
        this.stickers = stickers;
    }
}
