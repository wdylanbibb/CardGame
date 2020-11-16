package Card_Game;

import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImgAccessor {

    private static final String BASE_DIR = "carddata/imgs/";
    public static final String DEFAULT_IMG = "default";

    public static Pair<Image, String> getImage(String fileName) {
        try {
            return Pair.of(ImageIO.read(new File(BASE_DIR + fileName + ".png")), fileName);
        } catch (IOException e) {
            return Pair.of(defaultImg(), DEFAULT_IMG);
        }
    }

    public static Image defaultImg() {
        try {
            return ImageIO.read(new File(BASE_DIR + DEFAULT_IMG + ".png"));
        } catch (IOException e) {
            return null;
        }
    }
}
