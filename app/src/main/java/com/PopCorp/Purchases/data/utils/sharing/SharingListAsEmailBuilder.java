package com.PopCorp.Purchases.data.utils.sharing;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;

import com.PopCorp.Purchases.data.model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SharingListAsEmailBuilder implements SharingListBuilder {

    @Override
    public Intent getIntent(String name, String currency, List<ListItem> items) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + "?subject=" + Uri.encode(name) /*+ "&body=" + Uri.encode(String.valueOf(getItems(items, currency)))*/;
        Uri uri = Uri.parse(uriText);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_TEXT, (String.valueOf(getItems(items, currency))));
        intent.setData(uri);
        try {
            File file = createFile(name, currency, items);
            if (file != null) {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
            }
        } catch (IOException ignored) {
        }
        return intent;
    }

    private Spanned getItems(List<ListItem> items, String currency){
        String result = "<html>";
        for (ListItem item : items){
            result += item.getName();
            result += item.getComment().isEmpty() ? " " : " (" + item.getComment() + ") ";
            result += item.getCountString() + " " + item.getEdizm() + " по " + item.getCoastString() + " " + currency;
            result += item.getShop().isEmpty() ? "" : " в " + item.getShop();
            result += "<br>";
        }
        result += "<br>Отправлено из приложения <a href='https://play.google.com/store/apps/details?id=com.PopCorp.Purchases'>Покупки по акциям</a>.<br>" +
                "Если у Вас установлено данное приложение, нажмите на файл, чтобы загрузить список в приложение</html>";
        return Html.fromHtml(result);
    }

    private File createFile(String name, String currency, List<ListItem> itemsForSending) throws IOException {
        File directory = Environment.getExternalStorageDirectory();
        if (!isExternalStorageWritable()) {
            return null;
        }
        directory = new File(directory.getAbsolutePath() + "/Purchases");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }
        File listFile = new File(directory.getAbsolutePath() + "/" + name + ".purchaseslist");
        FileWriter writer = new FileWriter(listFile);
        writer.append(itemsToJSON(name, currency, itemsForSending));
        writer.flush();
        writer.close();
        return listFile;
    }

    static public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static final String JSON_LIST_NAME = "JSON_LIST_NAME";
    private static final String JSON_LIST_CURRENCY = "JSON_LIST_CURRENCY";

    private static final String JSON_ITEM_NAME = "JSON_ITEM_NAME";
    private static final String JSON_ITEM_COUNT = "JSON_ITEM_COUNT";
    private static final String JSON_ITEM_EDIZM = "JSON_ITEM_EDIZM";
    private static final String JSON_ITEM_COAST = "JSON_ITEM_COAST";
    private static final String JSON_ITEM_CATEGORY = "JSON_ITEM_CATEGORY";
    private static final String JSON_ITEM_SHOP = "JSON_ITEM_SHOP";
    private static final String JSON_ITEM_COMMENT = "JSON_ITEM_COMMENT";
    private static final String JSON_ITEM_BUYED = "JSON_ITEM_BUYED";
    private static final String JSON_ITEM_IMPORTANT = "JSON_ITEM_IMPORTANT";

    private String itemsToJSON(String name, String currency, List<ListItem> itemsForSending) {
        JSONArray array = new JSONArray();
        JSONObject list = new JSONObject();
        try {
            list.put(JSON_LIST_NAME, name);
            list.put(JSON_LIST_CURRENCY, currency);
        } catch (JSONException ignored) {
        }
        array.put(list);
        for (ListItem item : itemsForSending) {
            JSONObject object = new JSONObject();
            try {
                object.put(JSON_ITEM_NAME, item.getName());
                object.put(JSON_ITEM_COUNT, item.getCountString());
                object.put(JSON_ITEM_EDIZM, item.getEdizm());
                object.put(JSON_ITEM_COAST, item.getCoastString());
                object.put(JSON_ITEM_CATEGORY, item.getCategory().getName());
                object.put(JSON_ITEM_SHOP, item.getShop());
                object.put(JSON_ITEM_COMMENT, item.getComment());
                object.put(JSON_ITEM_BUYED, item.isBuyed());
                object.put(JSON_ITEM_IMPORTANT, item.isImportant());
            } catch (JSONException e) {
                continue;
            }
            array.put(object);
        }
        return array.toString();
    }
}
