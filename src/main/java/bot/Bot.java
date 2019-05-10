package bot;

import com.luciad.imageio.webp.WebPReadParam;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
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
import java.util.List;



public class Bot extends TelegramLongPollingBot {
    //320521360
    static long chatid = 314254027;
    static long own_chatId=314254027;
    static String info_for_zakaz = "";
    static List<Sticker> stickers = new ArrayList<>();
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
        temp = ImageIO.read(new File("src/main/resources/" + "StickerPackImage.png"));
    } catch(
    IOException e)

    {
        e.printStackTrace();
    }
        finalImg.createGraphics().

    drawImage(temp, sample.getWidth()/3,sample.getHeight()-130,null);
        }

    //Initializing the final image

    @Override
    public void onUpdateReceived(Update update) {
       if(update.hasMessage()) {
           Message message = update.getMessage();
           if (message != null && message.hasText()) {

               SendMessage sendMessage = new SendMessage();
               sendMessage.setChatId(message.getChatId());
               System.out.println(message.getChatId());
               replyKeyboardMarkup.setResizeKeyboard(true);
               replyKeyboardMarkup.setOneTimeKeyboard(true);

               replyKeyboardMarkup.setSelective(true);

               switch (message.getText()) {
                   case "/start":
                       stickers = new ArrayList<>();
                       number = 0;
                        count=1;
                        x=35;
                        y=10;

                       try {
                           System.out.println("start");
                           sendApiMethod(sendMessage.setText("Приветсвую я StickerPackBot, \n если тебе нужно распечатать стикера тогда сделай сначала макет").setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, stickers.size())));
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   case "О боте":
                       try {

                           sendApiMethod(sendMessage.setText("Этот бот создает макет Sticker Book из вашых стикеров").setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, stickers.size())));
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;

                   case "Оформить":
                       sendMessage.setChatId(chatid);

                       sendMessage.setText("Заказ про то шо коля хуй)");

                       try {
                           sendApiMethod(sendMessage);
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   case "Сделать макет":
                       try {
                           stickers = new ArrayList<>();

                           sendApiMethod(sendMessage.setText("Отправте 12 стикеров").setReplyMarkup(remakeButtons("hide", replyKeyboardMarkup, stickers.size())));
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;
                   default:
                       try {
                           if (message.getText().split(" ").length> 3) {
                               sendMessage.setChatId(chatid);
                               sendMessage.setText(message.getText());
                               execute(sendMessage);
                               sendMessage.setChatId(message.getChatId());
                               sendMessage.setText("Ваш заказ принят\n ожидайте с вами свяжуться\n не желаете попробовать ещё?");
                               sendApiMethod(sendMessage.setReplyMarkup(remakeButtons("/yes/no", replyKeyboardMarkup, stickers.size())));

                           } else {
                               sendMessage.setText("введите запрос /start ещё раз и повторите создание макета");
                               sendApiMethod(sendMessage.setReplyMarkup(remakeButtons(message.getText(), replyKeyboardMarkup, stickers.size())));
                           }
                       } catch (TelegramApiException e) {
                           e.printStackTrace();
                       }
                       break;

               }

           }
           if (message != null && message.hasSticker()) {
               sendPreviuw.setChatId(message.getChatId());
               System.out.println(message.getChatId());
               if (stickers.size() < 12) {
                   own_chatId=message.getChatId();
                   stickers.add(message.getSticker());

                   SendPhoto sendMessage = new SendPhoto();

                   sendMessage.setChatId(message.getChatId());

                   number++;
                   try {
                       getSticker(message, stickers.size());
                   } catch (MalformedURLException e) {
                       e.printStackTrace();
                   }
                   try {
                       DecodedWebP(stickers.size());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                   sendPreviuw = AddPhotoToTemplate(message);

                   try {
                       sendApiMethod(sendInlineKeyBoardMessage(message.getChatId(),1).setText("На макете осталось свободных мест  "
                               + (12 - stickers.size()) + " для стикеров стикеров"));

                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }

               }
               if (stickers.size() >= 12) {
                   try {
                       SendPhoto send = combineALLImages(message, "src/main/resources/", 12);
                       SendMessage sendMessage = new SendMessage();
                       sendMessage.setChatId(message.getChatId());

                       sendApiMethod(sendInlineKeyBoardMessage(message.getChatId(),2).setText("Теперь давайте оформим заказ"));

                       send.setChatId(chatid);
                       execute(send);
                       Thread.sleep(100);

                       stickers = new ArrayList<>();
                       number = 0;


                   } catch (IOException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   stickers = new ArrayList<>();
               }
           }
       }
       if(update.hasCallbackQuery())
       {
           switch (update.getCallbackQuery().getData())
           {
               case "preview":
                   try {
                       execute(sendPreviuw);
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   break;
               case "enter":
                   SendMessage sendMessage =new SendMessage();
                   sendMessage.setChatId(own_chatId);
                   sendMessage.setText("Напишите свое Ф.И.О.\nсвой город\n номер телефона\n  и отделение новой почты со способом оплаты ");
                   try {
                       execute(sendPreviuw.setChatId(own_chatId));
                       execute(sendPreviuw.setChatId(chatid));
                       execute(sendMessage);

                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                   break;

           }

       }
    }
    public static SendMessage sendInlineKeyBoardMessage(long chatId,int n) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
      if(n==1) {
          InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();

          InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
          inlineKeyboardButton1.setText("Превью");
          inlineKeyboardButton1.setCallbackData("preview");
          inlineKeyboardButton2.setText("Оформить");
          inlineKeyboardButton2.setCallbackData("enter");
          List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

          keyboardButtonsRow1.add(inlineKeyboardButton1);
          keyboardButtonsRow1.add(inlineKeyboardButton2);
          List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
          rowList.add(keyboardButtonsRow1);

          inlineKeyboardMarkup.setKeyboard(rowList);
      }
      if(n==2)
      {
          InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
          inlineKeyboardButton1.setText("Оформить");
          inlineKeyboardButton1.setCallbackData("enter");
          List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

          keyboardButtonsRow1.add(inlineKeyboardButton1);
          List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
          rowList.add(keyboardButtonsRow1);

          inlineKeyboardMarkup.setKeyboard(rowList);

      }
        return new SendMessage().setChatId(chatId).setText("Пример").setReplyMarkup(inlineKeyboardMarkup);
    }

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
        File localFile = new File("test_pic/sticker" + numbers + ".webp");

        try {
            FileUtils.copyInputStreamToFile(fileUrl, localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(remakeButtons(sendMessage.getText(), replyKeyboardMarkup, number));
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);

        try {

            sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    public synchronized ReplyKeyboardMarkup remakeButtons(String s, ReplyKeyboardMarkup replyKeyboardMarkup, int n) {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        keyboardButton1.setText("О боте");
        keyboardButton2.setText("Сделать макет");
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
                keyboardRow1.add(keyboardButton1.setText("Сделать макет"));

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
            case "Сделать макет":
                keyboardRow1.clear();


                keyboardRow1.add(keyboardButton2.setText("Осталось " + n + "/12"));
                return replyKeyboardMarkup;
            case "Оформить":
                keyboardRow1.clear();
                keyboardButton1.setText("О боте");
                keyboardButton2.setText("Сделать макет");
                keyboardRow1.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                return replyKeyboardMarkup;
            default:
                return replyKeyboardMarkup;

        }


    }

    public void DecodedWebP(int number) throws IOException {
        String inputWebpPath = "test_pic/sticker" + number + ".webp";

        String outputPngPath = "src/main/resources/sticker" + number + ".png";

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


    public  SendPhoto AddPhotoToTemplate(Message message)
    {
        SendPhoto sen=new SendPhoto();
        sen.setChatId(message.getChatId());

        int wid=sample.getWidth()/3;
        int hei=sample.getWidth()/4;


        try {
            temp = ImageIO.read(new File("src/main/resources/sticker"+count+".png"));
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
        System.out.println("src/main/resources/sticker" + count + ".png");

        File final_Image = new File("src/main/resources/stickerpack.png");
        try {
            ImageIO.write(finalImg, "png", final_Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;
        return new SendPhoto().setChatId(message.getChatId()).setPhoto(new File("src/main/resources/stickerpack.png")).setCaption(info_for_zakaz);

    }


    private static SendPhoto combineALLImages(Message message,String screenNames, int screens) throws IOException, InterruptedException {




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