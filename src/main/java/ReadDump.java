import fr.bmartel.pcapdecoder.PcapDecoder;
import fr.bmartel.pcapdecoder.structure.types.IPcapngType;
import fr.bmartel.pcapdecoder.structure.types.inter.IEnhancedPacketBLock;

import java.util.ArrayList;

public class ReadDump {

    public ReadDump(){

    }

    public static ArrayList<String> getDump(){
        PcapDecoder decoder = new PcapDecoder("C:/ibodump.pcapng");
        decoder.decode();
        ArrayList<IPcapngType> sectionList = decoder.getSectionList();
        ArrayList<IEnhancedPacketBLock> packetList = new ArrayList<>();
        ArrayList<String> byteStrings = new ArrayList<>();
        System.out.println("Reading dump...");
        for (int i = 0; i <= 180000; i++){
            if (sectionList.get(i) instanceof  IEnhancedPacketBLock){
                IEnhancedPacketBLock section = (IEnhancedPacketBLock) sectionList.get(i);
                packetList.add(section);
            }
        }

        for (IEnhancedPacketBLock block : packetList){
            byte bytes [] = block.getPacketData();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes){
                sb.append(String.format("%02X", b));
            }
            String byteString = sb.toString();
            if (byteString.startsWith("FFFFFF")){
                byteStrings.add(byteString);
            }

        }


        return byteStrings;
    }

}
