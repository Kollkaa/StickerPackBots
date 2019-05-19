package bot;

import com.luciad.imageio.webp.WebPReadParam;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


public class Bot extends TelegramLongPollingBot {
    //799964941
    TreeMap<Long,Users> user=new TreeMap<>();
    Integer count_user=1;
    static String screenName="src/main/resources/";
    static long chatid = 314254027;
    static long own_chatId=314254027;
    static String info_for_zakaz = "";
    static String info_for_start="Привет! \n" +
            "Я распечатаю и отправлю тебе твои любимые стикеры из Телеграмма!\n" +
            "\n" +
            "\uD83D\uDC4C 12 стикеров на листе А5\n" +
            "\uD83D\uDCB3 Стоимость одного набора - 50 грн\n" +
            "\uD83D\uDCE6 Доставка по всей Украине!\n" +
            "\n" +
            "Нажми «Создать макет\" что бы начать!";
    private int x = 35;
    private int y = 10;
    int count = 1;
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    static int number = 1;
    public ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    static BufferedImage sample;
    static BufferedImage finalImg;
    BufferedImage temp;
    private SendPhoto sendPreviuw;

    {
sendPreviuw=new SendPhoto();
        try
    {
        sample = ImageIO.read(new File("src/main/resources/" + "tamplate.png"));

    } catch(
    IOException e)

    {
        e.printStackTrace();
    }

     finalImg = new BufferedImage(sample.getWidth() * 1, sample.getHeight() * 1, sample.getType());

        try

    {
        temp = ImageIO.read(new File(screenName + "StickerPackImage.png"));
    } catch(
    IOException e)

    {
        e.printStackTrace();
    }
        finalImg.createGraphics().

    drawImage(temp, sample.getWidth()/3,sample.getHeight()-130,null);
        user.put((long)41244123,new Users((long) 314254027,new ArrayList<Sticker>(),"Kolia"));
        }
    //Initializing the final image
    Users usere;
     public void GetInfoFromSite()
     {
         URL url;
         InputStream is = null;
         DataInputStream dis;
         String line;
             try {
                 url = new URL("https://privatbank.ua/ru/sendmoney");
                 is = url.openStream();  // throws an IOException
                 dis = new DataInputStream(new BufferedInputStream(is));

                 while ((line = dis.readLine()) != null) {
                     if(line.indexOf("receiver_card")>=0)
                     System.out.println(line);
                 }
             } catch (MalformedURLException mue) {
                 mue.printStackTrace();
             } catch (IOException ioe) {
                 ioe.printStackTrace();
             } finally {
                 try {
                     is.close();
                 } catch (IOException ioe) {
                     // nothing to see here
                 }
             }

     }
    @Override
    public void onUpdateReceived(Update update) {

       if(update.hasMessage()) {
           System.out.println(update.getMessage().getFrom().getFirstName());
           Message message = update.getMessage();
           screenName="src/main/resources/"; //Own path to package users
           if (message != null && message.hasText()) {
               try {
                     if(!user.containsKey(message.getChatId()))
                   {
                       user.put(message.getChatId(), new Users(message.getChatId(), new ArrayList<Sticker>(),message.getFrom().getFirstName()));
                   }
                     if (user.containsKey(message.getChatId()))
                     {
                         own_chatId=message.getChatId();
                         usere=user.get(own_chatId);
                     }




               }catch (Exception e)
               {System.out.println(e.getStackTrace());}

               SendMessage sendMessage = new SendMessage();
               sendMessage.setChatId(message.getChatId());

               System.out.println(message.getChatId());

               replyKeyboardMarkup.setResizeKeyboard(true);
               replyKeyboardMarkup.setOneTimeKeyboard(true);
               replyKeyboardMarkup.setSelective(true);

               switch (message.getText()) {
                   case "/start":
                   Rebuild();


                       try {
                           System.out.println("start");
                          execute( (new SendPhoto().setChatId(own_chatId).setPhoto(new File("src/main/resources/start.jpg")).setCaption(info_for_start))
                            .setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, usere.getStickers().size())));
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   case "О боте":
                        System.out.println("about bot");
                       try {
                           sendApiMethod(sendMessage.setText("Этот бот создает макет Sticker Book из вашых стикеров").setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, usere.getStickers().size())));
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   case "Оформить":
                       System.out.println("about oformit");
                       sendMessage.setChatId(chatid);
                       sendMessage.setText("Заказ про то шо коля хуй)");
                       try {
                           sendApiMethod(sendMessage);
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   case "Создать макет":
                       System.out.println("about sdelat maket");
                       try {
                           usere.setStickers( new ArrayList<Sticker>());
                           usere.setZakaz();
                           sendApiMethod(sendMessage.setText("Отправте 12 стикеров").setReplyMarkup(remakeButtons("hide", replyKeyboardMarkup, usere.getStickers().size())));
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   default:

                       try {
                           if (message.getText()!=null) {
                               sendMessage.setChatId(chatid);
                               sendMessage.setText(message.getText());
                               execute(sendMessage);
                               execute((sendInlineKeyBoardMessage(own_chatId,3)
                                      .setText("Теперь давайте выберем способ оплаты")));
                               user.get(own_chatId).setZakaz();
                           } else {
                               sendMessage.setText("введите запрос /start ещё раз и повторите создание макета");
                               sendApiMethod(sendMessage.setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, usere.getStickers().size())));
                           }
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
               }
           }//To worker make primary message
           if (message != null && message.hasSticker()) {
               int counts=0;
               for (Long key :user.keySet()) {
                   counts++;
                   screenName="src/main/resources/";
                   System.out.println(key);
                   if ((counts ) % 5 == 0) {
                       screenName += "6_zak/";
                       own_chatId =key;
                   }
                   if ((counts ) % 4 == 0) {
                       screenName += "5_zak/";
                       own_chatId =key;
                   }
                   if ((counts ) % 6 == 0) {
                       screenName += "4_zak/";
                       own_chatId =key;
                   }
                   if ((counts ) % 3 == 0) {
                       screenName += "3_zak/";
                       own_chatId =key;
                   }
                   if ((counts ) % 2 == 0) {
                       screenName += "2_zak/";
                       own_chatId =key;
                   }
                   if ((counts )%1==0)
                   {
                       screenName += "1_zak/";
                       own_chatId=key;
                   }
                   if(counts==7)
                       counts=0;
               }
               sendPreviuw.setChatId(message.getChatId());
               System.out.println(message.getChatId());
               if (usere.getStickers().size() < 12) {
                   if(!user.containsKey(update.getMessage().getChatId()))
                   {
                       user.put(update.getMessage().getChatId(), new Users(update.getMessage().getChatId(), new ArrayList<Sticker>(),message.getFrom().getFirstName()));
                   }
                   if (user.containsKey(update.getMessage().getChatId()))
                   {
                       own_chatId=update.getMessage().getChatId();
                       usere=user.get(own_chatId);
                   }
                   own_chatId=message.getChatId();
                   usere.getStickers().add(message.getSticker());
                   SendPhoto sendMessage = new SendPhoto();
                   sendMessage.setChatId(message.getChatId());
                   number++;

                   try {
                       getSticker(message, usere.getStickers().size());
                   } catch (MalformedURLException e) {
                       e.printStackTrace();
                   }
                   try {
                       DecodedWebP(usere.getStickers().size());
                       sendPreviuw = AddPhotoToTemplate(message);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }


                   try {
                       sendApiMethod(sendInlineKeyBoardMessage(message.getChatId(),1).setText("На макете осталось свободных мест  "
                               + (12 - usere.getStickers().size()) + " для стикеров"));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
               }
               if (usere.getStickers().size() >= 12) {
                   if(!user.containsKey(update.getMessage().getChatId()))
                   {
                       user.put(update.getMessage().getChatId(), new Users(update.getMessage().getChatId(), new ArrayList<Sticker>(),message.getFrom().getFirstName()));
                   }
                   if (user.containsKey(update.getMessage().getChatId()))
                   {
                       own_chatId=update.getMessage().getChatId();
                       usere=user.get(own_chatId);
                   }
                   try {
                       SendPhoto send = combineALLImages(message, screenName, 12);
                       SendMessage sendMessage = new SendMessage();
                       sendMessage.setChatId(message.getChatId());
                       sendApiMethod(sendInlineKeyBoardMessage(message.getChatId(),2).setText("Теперь давайте оформим заказ"));
                       send.setChatId(chatid);

                       System.out.println("my photo");
                       execute(send);
                       Thread.sleep(100);


                   } catch (IOException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }



               }
           } //To worker make stickers messege
       }
       if(update.hasCallbackQuery()){

           switch (update.getCallbackQuery().getData())
           {
               case "preview":
                   System.out.println("about preview");
                   try {
                       execute(sendPreviuw);
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   break;
               case "enter":
                   System.out.println("about enter");
                   SendMessage sendMessage =new SendMessage();
                   sendMessage.setChatId(own_chatId);
                   sendMessage.setText("Напишите свое Ф.И.О. \uD83D\uDC68\u200D\uD83D\uDCBB \nсвой город\n номер телефона\n службу доставки и отделение почты");

                   try {

                       System.out.println("worker photo");
                       execute(new SendDocument().setDocument(new File(screenName+"stickerpack.png")).setChatId(chatid));
                       execute(sendPreviuw.setChatId(chatid));
                       System.out.println("message photo");
                       execute(sendMessage);

                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   System.out.println("rebuild photo");
                   Rebuild();
                   break;
               case "otmena":

                   System.out.println("about otmena");
                   try {
                       execute(new SendMessage().setChatId(own_chatId).setText("Нажмите /start что б опять начать"));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   break;

               case "naloz":
                   SendMessage sendMessa=new SendMessage();
                   sendMessa.setChatId(chatid);
                   sendMessa.setText("Наложеным");
                   try {
                       execute(sendMessa);
                       execute(sendInlineKeyBoardMessage(usere.getChatid(),4).setText("Вам понравился наш сервис \uD83D\uDE80?"));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                    Rebuild();

                   break;
               case "na_carty":
                   try {
                       execute(sendInlineKeyBoardMessage(usere.getChatid(),5).setText("Выбирите банк :"));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }

                   break;
               case "privat":
                   SendMessage sendMessas1=new SendMessage();
                   sendMessas1.setChatId(chatid);
                   sendMessas1.setText("На карту привата");
                   try {
                       execute(sendMessas1);
                   }catch (Exception e)
                   {
                       System.out.println(e.getStackTrace());
                   }

                   sendMessas1.setChatId(own_chatId);
                   sendMessas1.setText("Інтернет картка: \n 5169360005626969\n"+"https://privatbank.ua/ru/sendmoney");

                   try {
                       execute(sendMessas1);
                       execute(sendInlineKeyBoardMessage(usere.getChatid(),4).setText("Вам понравился наш сервис?"));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }



                   Rebuild();
                   break;
               case "mono":
                   SendMessage sendMessas2=new SendMessage();
                   sendMessas2.setChatId(chatid);
                   sendMessas2.setText("На карту монобанка");
                   try {
                       execute(sendMessas2);
                   }catch (Exception e)
                   {
                       System.out.println(e.getStackTrace());
                   }

                   sendMessas2.setChatId(own_chatId);
                   sendMessas2.setText("Інтернет картка: \n 5169360005626969\n"+"https://www.monobank.com.ua");

                   try {
                       execute(sendMessas2);
                       execute(sendInlineKeyBoardMessage(usere.getChatid(),4).setText("Вам понравился наш сервис?"));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   Rebuild();
                   break;
               case "yes":

                   try {
                       execute(sends("@"+usere.getName()+": "+own_chatId+" Этому пользователю понравился сервис ",chatid ));
                       execute( (new SendPhoto().setChatId(own_chatId).setPhoto(new File("src/main/resources/start.jpg")).setCaption(info_for_start))
                               .setReplyMarkup(remakeButtons("/start", replyKeyboardMarkup, usere.getStickers().size())));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   break;
               case"no":
                   try {
                       execute(sends( own_chatId+" Этому пользователю не понравился сервис ",chatid));
                       execute( (new SendPhoto().setChatId(own_chatId).setPhoto(new File("src/main/resources/start.jpg")).setCaption(info_for_start))
                               .setReplyMarkup(remakeButtons("/start", replyKeyboardMarkup, usere.getStickers().size())));
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   break;
           }
       } //Call inlineButton
    }

    public static boolean openWebpage(URI uri){
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {

            try {

                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Send primary message
    public SendMessage sends(String s,Long chat){
        SendMessage ser=new SendMessage();
        ser.setChatId(chat);
        ser.setText(s);
        return ser;


    }
    //Rebuild all pictures to new
    public  void Rebuild(){
        try {

            sample = ImageIO.read(new File(screenName + "tamplate.png"));
            finalImg = new BufferedImage(sample.getWidth() * 1, sample.getHeight() * 1, sample.getType());
            temp = ImageIO.read(new File(screenName + "StickerPackImage.png"));
            finalImg.createGraphics().
                    drawImage(temp, sample.getWidth() / 3, sample.getHeight() - 130, null);
            sendPreviuw = new SendPhoto();

        }
        catch (Exception e)
        {System.out.println(e.getStackTrace());}
        usere.setStickers( new ArrayList<Sticker>());
        number = 0;
        count=1;
        x=35;
        y=10;
    }
    //Remake inlineButtons
    public static SendMessage sendInlineKeyBoardMessage(long chatId,int n) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

      if(n==1) {

          InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
          InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
          InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();

          inlineKeyboardButton1.setText("Превью \uD83D\uDDBC");
          inlineKeyboardButton1.setCallbackData("preview");
          inlineKeyboardButton2.setText("Оформить ✅");
          inlineKeyboardButton2.setCallbackData("enter");
          inlineKeyboardButton3.setText("Начать с начала ↩️");
          inlineKeyboardButton3.setCallbackData("otmena");
          List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
          List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
          keyboardButtonsRow1.add(inlineKeyboardButton1);
          keyboardButtonsRow1.add(inlineKeyboardButton2);
          keyboardButtonsRow2.add(inlineKeyboardButton3);
          List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
          rowList.add(keyboardButtonsRow1);
          rowList.add(keyboardButtonsRow2);
          inlineKeyboardMarkup.setKeyboard(rowList);
      }
      if(n==2)
      {
          InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
          inlineKeyboardButton1.setText("Оформить ✅");
          inlineKeyboardButton1.setCallbackData("enter");
          List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
          keyboardButtonsRow1.add(inlineKeyboardButton1);
          List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
          rowList.add(keyboardButtonsRow1);
          inlineKeyboardMarkup.setKeyboard(rowList);
      }
      if(n==3)
      {
          InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
          InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
          inlineKeyboardButton1.setText("При получении \uD83D\uDECD");
          inlineKeyboardButton1.setCallbackData("naloz");
          inlineKeyboardButton2.setText("На карту \uD83D\uDCB3 ");
          inlineKeyboardButton2.setCallbackData("na_carty");

          List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
          keyboardButtonsRow1.add(inlineKeyboardButton1);
          keyboardButtonsRow1.add(inlineKeyboardButton2);

          List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
          rowList.add(keyboardButtonsRow1);
          inlineKeyboardMarkup.setKeyboard(rowList);
      }
      if(n==4)
      {
          InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
          InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
          inlineKeyboardButton1.setText("Да\uD83D\uDC4D");
          inlineKeyboardButton1.setCallbackData("yes");
          inlineKeyboardButton2.setText("Нет\uD83D\uDC4E");
          inlineKeyboardButton2.setCallbackData("no");

          List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
          keyboardButtonsRow1.add(inlineKeyboardButton1);
          keyboardButtonsRow1.add(inlineKeyboardButton2);

          List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
          rowList.add(keyboardButtonsRow1);
          inlineKeyboardMarkup.setKeyboard(rowList);

      }
        if(n==5)
        {
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("На карту Приват банка");
            inlineKeyboardButton1.setCallbackData("privat");
            inlineKeyboardButton2.setText("На карту Моно банка");
            inlineKeyboardButton2.setCallbackData("mono");

            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton1);
            keyboardButtonsRow1.add(inlineKeyboardButton2);

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);
            inlineKeyboardMarkup.setKeyboard(rowList);

        }

        return new SendMessage().setChatId(chatId).setText("Пример").setReplyMarkup(inlineKeyboardMarkup);
    }
    //Getter stickers from message
    public void getSticker(Message message, int numbers) throws MalformedURLException {
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
        File localFile = new File(screenName+"sticker" + numbers + ".webp");

        try {
            FileUtils.copyInputStreamToFile(fileUrl, localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Remake replyButtons
    public synchronized ReplyKeyboardMarkup remakeButtons(String s, ReplyKeyboardMarkup replyKeyboardMarkup, int n) {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        keyboardButton1.setText("О боте");
        keyboardButton2.setText("Создать макет");
        keyboardRow1.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        List<KeyboardRow> klava = new ArrayList<KeyboardRow>();
        klava.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(klava);
        switch (s) {
            case"hide":
                keyboardRow1.clear();
                klava = new ArrayList<KeyboardRow>();
                klava.add(keyboardRow1);
                replyKeyboardMarkup.setKeyboard(klava);
               return replyKeyboardMarkup;
            case "да":
                return replyKeyboardMarkup;
            case "нет":
                return replyKeyboardMarkup;
            case "/start":
                return replyKeyboardMarkup;
            case "О боте":
                keyboardRow1.clear();
                keyboardRow1.add(keyboardButton1.setText("Создать макет"));
                return replyKeyboardMarkup;
            case "/all":
                keyboardRow1.clear();
                klava.add(keyboardRow1);
                replyKeyboardMarkup.setKeyboard(klava);
                return replyKeyboardMarkup;
            case "/yes/no":
                keyboardRow1.clear();
                keyboardButton1.setText("да");
                keyboardButton2.setText("нет");
                keyboardRow1.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                return replyKeyboardMarkup;
            case "Создать макет":
                keyboardRow1.clear();
                keyboardRow1.add(keyboardButton2.setText("Осталось " + n + "/12"));
                return replyKeyboardMarkup;
            case "Оформить":
                keyboardRow1.clear();
                keyboardButton1.setText("О боте");
                keyboardButton2.setText("Создать макет");
                keyboardRow1.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                return replyKeyboardMarkup;
            default:
                return replyKeyboardMarkup;

        }


    }
    //Convert webp to png
    public void DecodedWebP(int number) throws IOException {
        String inputWebpPath = screenName+"sticker" + number + ".webp";

        String outputPngPath = screenName+"sticker" + number + ".png";

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
    // Add new Sticker for Sticker pack
    public  SendPhoto AddPhotoToTemplate(Message message){
        SendPhoto sen=new SendPhoto();
        sen.setChatId(message.getChatId());

        int wid=sample.getWidth()/3;
        int hei=sample.getWidth()/4;


        try {
            temp = ImageIO.read(new File(screenName+"sticker"+count+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(x+" "+y);
        if(count%3==0)
        {
            finalImg.createGraphics().drawImage(temp, x,y , null);
            x=35;
            y+=hei+150;
        }else{

        finalImg.createGraphics().drawImage(temp, x,y , null);
            x+=wid;
        }
        System.out.println(screenName+"sticker" + count + ".png");

        File final_Image = new File(screenName+"stickerpack.png");
        try {
            ImageIO.write(finalImg, "png", final_Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;
        return new SendPhoto().setChatId(message.getChatId()).setPhoto(new File(screenName+"stickerpack.png")).setCaption(info_for_zakaz);

    }
    // Send finished result packSticker
    private static SendPhoto combineALLImages(Message message,String screenNames, int screens) throws IOException, InterruptedException {




        File final_Image = new File(screenNames+"stickerpack.png");
        ImageIO.write(finalImg, "png", final_Image);


        return new SendPhoto().setChatId(message.getChatId()).setPhoto(new File(screenName+"stickerpack.png")).setCaption(info_for_zakaz);
    }
    //UserName
    @Override
    public String getBotUsername() {
        return "@StickersPackBot";
    }

    //Tocken
    @Override
    public String getBotToken() {
        return "874387005:AAF2YNgabQejDE5NArh3iw6elIdR62sDyZE";
    }
}