package example.readfile.run;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ReadFile {
    
    public List<String> loadFile() throws Exception{
       
        List<String> filenames = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/resource/somefile.yml")));
        String resource;

        while ((resource = br.readLine()) != null) {
            filenames.add(resource);
        }
        return filenames;
    
    }

    public static void main(String[] args) throws Exception {
        new ReadFile().loadFile().forEach(System.out::println);
    }
    
}
