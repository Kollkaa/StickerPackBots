package Bot;

import com.luciad.imageio.webp.WebPReadParam;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.io.FileUtils.getFile;

public class Bot extends TelegramLongPollingBot {
     static long chatid=314254027;
    static String info_for_zakaz="";
    List<Sticker> stickers = new ArrayList<>();
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    static int number=1;
    public  ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
    public static void main(String... args) {
        System.out.println("start");
        ApiContextInitializer.init();
        System.out.println("init");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        System.out.println("contunie");
        try//
        {
            Bot bot=new Bot();
            telegramBotsApi.registerBot(bot);


        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message=update.getMessage();
        if(message!=null && message.hasText()) {

            SendMessage sendMessage=new SendMessage();
            sendMessage.setChatId(message.getChatId());
            System.out.println(message.getChatId());
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            replyKeyboardMarkup.setSelective(true);
            if(replyKeyboardMarkup.getKeyboard().size()==1)
            {
                sendMessage.setText("Заказ оформлен, ожидайте вскоре с вами свяжуться");
                try {
                    sendApiMethod(sendMessage.setReplyMarkup(remakeButtons("Оформить", replyKeyboardMarkup, number)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }//////
            switch (message.getText()) {
                   case   "О боте:" :
                      try {

                        sendApiMethod(sendMessage.setText("Этот бот создает макет Sticker Book из вашых стикеров").setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, number)));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                    case  "Оформить:"  :
                         sendMessage.setChatId(chatid);

                         sendMessage.setText("Заказ про то шо коля хуй)");

                         try {
                             sendApiMethod(sendMessage);
                         } catch (TelegramApiException e) {
                             e.printStackTrace();
                         }
                       break;
                      case  "Сделать макет:"  :
                       try{


                           sendApiMethod(sendMessage.setText("Отправте 12 стикеров").setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, number)));
                           } catch (TelegramApiException e) {
                           e.printStackTrace();
                           }
                        break;
                        default:
                          try {
                              if (message.getText().length()>20)
                              {
                                  sendMessage.setChatId(chatid);
                                  sendMessage.setText(message.getText());
                                  execute(sendMessage);
                                  sendMessage.setChatId(message.getChatId());
                                  sendMessage.setText("Ваш заказ принят\n ожидайте с вами свяжуться\n не желаете попробовать ещё?");
                                  sendApiMethod(sendMessage.setReplyMarkup(remakeButtons("/yes/no", replyKeyboardMarkup, number)));

                              }
                              else {
                                  sendMessage.setText("----");
                                  sendApiMethod(sendMessage.setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, number)));
                              }
                          } catch (TelegramApiException e) {
                                 e.printStackTrace();
                          }
                          break;

            }

        }
        if(message!=null && message.hasSticker())
        {

            System.out.println(message.getChatId());
            if (stickers.size()!=12){
                stickers.add(message.getSticker());
                SendMessage sendMessage=new SendMessage();
                sendMessage.setText("next");
                sendMessage.setChatId(message.getChatId());
                try {
                    sendApiMethod(sendMessage.setReplyMarkup(remakeButtons("Сделать макет:", replyKeyboardMarkup, number)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                number++;
            try {
                    getSticker(message, stickers.size() );
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    DecodedWebP(stickers.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//

            }
            if(stickers.size()==12)
            {
                try {
                    SendPhoto send=   combineALLImages(message,"src/main/resources/", 12);
                    SendMessage sendMessage=new SendMessage();
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Теперь давайте оформим заказ");
                    sendApiMethod(sendMessage.setReplyMarkup(remakeButtons("/all", replyKeyboardMarkup, number)));
                execute(send);
                Thread.sleep(100);
                    sendMessage.setText("Напишите свое Ф.И.О.\nсвой город\n  и отделение новой почты ");
                    sendApiMethod(sendMessage.setReplyMarkup(remakeButtons("/all", replyKeyboardMarkup, number)));

                    number=0;


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                stickers=new ArrayList<>();
            }
        }

    }

    public void getSticker(Message message,int numbers) throws MalformedURLException {
        GetFile getFile = new GetFile();
        getFile.setFileId(message.getSticker().getFileId());

        org.telegram.telegrambots.meta.api.objects.File file = null;
        try {
            file = execute(getFile);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        InputStream fileUrl = null;
        try {
            fileUrl = new URL(file.getFileUrl(getBotToken())).openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File localFile = new File("test_pic/sticker"+numbers+".webp");

        try {
            FileUtils.copyInputStreamToFile(fileUrl, localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(remakeButtons(sendMessage.getText(),replyKeyboardMarkup,number));
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);

        try {

            sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    public synchronized ReplyKeyboardMarkup remakeButtons(String s, ReplyKeyboardMarkup replyKeyboardMarkup,int n) {
        KeyboardRow keyboardRow1=new KeyboardRow();
        KeyboardButton keyboardButton1=new KeyboardButton();
        KeyboardButton keyboardButton2=new KeyboardButton();
        keyboardButton1.setText("О боте:");
        keyboardButton2.setText("Сделать макет:");
        keyboardRow1.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        List<KeyboardRow>klava=new ArrayList <KeyboardRow>();
        klava.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(klava);
        switch (s){
            case "да":
                return replyKeyboardMarkup;
            case "нет":
                return replyKeyboardMarkup;
            case "/start":
                return replyKeyboardMarkup;

                case "О боте:":
                keyboardRow1.clear();keyboardRow1.add(keyboardButton1.setText("Сделать макет:"));

                    return replyKeyboardMarkup;
            case"/all":
                keyboardRow1.clear();
                klava.add(keyboardRow1);
                    replyKeyboardMarkup.setKeyboard(klava);
                return replyKeyboardMarkup;
            case "/yes/no":
                keyboardRow1.clear();
                keyboardButton1.setText("да");
                keyboardButton1.setText("нет");

                return replyKeyboardMarkup;
                case "Сделать макет:":keyboardRow1.clear();


                keyboardRow1.add(keyboardButton2.setText("Осталось "+n+"/12"));
                    return replyKeyboardMarkup;
            case "Оформить:":keyboardRow1.clear();
                keyboardButton1.setText("О боте:");
                keyboardButton2.setText("Сделать макет:");
                keyboardRow1.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                return replyKeyboardMarkup;
                default:
                    return replyKeyboardMarkup;

        }






    }

    public void DecodedWebP(int number) throws IOException {
        String inputWebpPath = "test_pic/sticker"+number+".webp";

        String outputPngPath = "src/main/resources/sticker"+number+".png";

        // Obtain a WebP ImageReader instance
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

        // Configure decoding parameters
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);

        // Configure the input on the ImageReader
        reader.setInput(new FileImageInputStream(new File(inputWebpPath)));

        // Decode the image
        BufferedImage image = reader.read(0, readParam);

        ImageIO.write(image, "png", new File(outputPngPath));


    }

    public SendMessage sendMessage(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(s);



        try {

            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return sendMessage;
    }

    private static SendPhoto combineALLImages(Message message,String screenNames, int screens) throws IOException, InterruptedException {
        int rows = screens/3;
        int cols = screens/4;
        int chunks = rows * cols ;

        File[] imgFiles = new File[screens];
        int i =0 ;

        ArrayList<String> images=new ArrayList<String>();
        //screenNames == /test/stickers_png//
        images.add(screenNames+"sticker1.png");
        images.add(screenNames+"sticker2.png");
        images.add(screenNames+"sticker3.png");
        images.add(screenNames+"sticker4.png");
        images.add(screenNames+"sticker5.png");
        images.add(screenNames+"sticker6.png");
        images.add(screenNames+"sticker7.png");
        images.add(screenNames+"sticker8.png");
        images.add(screenNames+"sticker9.png");
        images.add(screenNames+"sticker10.png");
        images.add(screenNames+"sticker11.png");
        images.add(screenNames+"sticker12.png");

        for (String image : images) {
            imgFiles[i++]=new File(image);

        }

        BufferedImage sample = ImageIO.read(new File(screenNames+"tamplate.png"));
        //Initializing the final image
        BufferedImage finalImg = new BufferedImage(sample.getWidth()*1 , sample.getHeight()*1 , sample.getType());
        System.out.println(sample.getWidth()+" "+sample.getHeight());
        int index = 0;
        int wid=sample.getWidth()/3;
        int hei=sample.getWidth()/4;
        int x=35;
        int y=0;
        for (int t=0;t<images.size()/3;t++)

        {
            for (int j = 0; j < images.size()/4; j++) {
                BufferedImage temp = ImageIO.read(imgFiles[index++]);


                System.out.println(x+" "+y);
                finalImg.createGraphics().drawImage(temp, x,y , null);
                System.out.println(screenNames + j + ".png");
                x+=wid;

            }
            x=35;
            y+=hei+120;
        }
            BufferedImage temp=ImageIO.read(new File(screenNames+"StickerPackImage.png"));
        finalImg.createGraphics().drawImage(temp, sample.getWidth()/3,sample.getHeight()-130 , null);

        File final_Image = new File("src/main/resources/stickerpack.png");
        ImageIO.write(finalImg, "png", final_Image);


        return new SendPhoto().setChatId(message.getChatId()).setPhoto(new File("src/main/resources/stickerpack.png")).setCaption(info_for_zakaz);
    }

    @Override
    public String getBotUsername() {
        return "@ExampleStickerPackbot";
    }


    @Override
    public String getBotToken() {
        return "854912396:AAH_mR4yIqkVfyf6P9kpiMnjMe71FtqpdAk";
    }
}