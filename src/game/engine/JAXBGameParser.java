package game.engine;


import descriptor.BattleShipGame;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JAXBGameParser {

    public static BattleShipGame loadGameFromXML(String fileName) throws FileNotFoundException, FileNotXmlFormat, JAXBException {
        File f = new File(fileName);
        return loadGameFormFile(f);
    }

    public static void validateFile(File f) throws FileNotFoundException, FileNotXmlFormat {
        if (!f.exists()) {
            throw new FileNotFoundException(f.getName() + " Not Found.");
        }

        if (!f.isFile() || !f.getPath().endsWith(".xml")) {
            throw new FileNotXmlFormat();
        }
    }

    public static BattleShipGame loadGameFormFile(File file) throws FileNotFoundException, FileNotXmlFormat, JAXBException {
        validateFile(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        BattleShipGame game = (BattleShipGame) jaxbUnmarshaller.unmarshal(file);
        return game;
    }
}

