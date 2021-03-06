package engine.managers.game;


import descriptor.BattleShipGame;
import engine.exceptions.InvalidFileFormatException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JAXBGameParser {

    public static BattleShipGame loadGameFromXML(String fileName) throws FileNotFoundException, InvalidFileFormatException, JAXBException {
        File f = new File(fileName);
        if (f != null) {
            return loadGameFromFile(f);
        }
        return null;
    }

    public static BattleShipGame loadGameFromFile(File file) throws FileNotFoundException, InvalidFileFormatException, JAXBException {
        validateFile(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        BattleShipGame game = (BattleShipGame) jaxbUnmarshaller.unmarshal(file);
        return game;
    }

    public static BattleShipGame loadGameFromStream(InputStream stream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        BattleShipGame game = (BattleShipGame) jaxbUnmarshaller.unmarshal(stream);
        return game;
    }

    private static void validateFile(File f) throws FileNotFoundException, InvalidFileFormatException {
        if (!f.exists()) {
            throw new FileNotFoundException(f.getName() + " Not Found.");
        }

        if (!f.isFile() || !f.getPath().endsWith(".xml")) {
            throw new InvalidFileFormatException();
        }
    }

}

