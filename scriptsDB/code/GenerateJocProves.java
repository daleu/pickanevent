import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by p4triot on 28/11/2016.
 */

public class GenerateJocProves {


    public static void main(String[] args) {
        generacio_joc_proves();
        printJson();
    }

    private static final int multiplier = 1;
    //valors
    private static final int num_users = 100*multiplier;
    private static final int num_cms = 10*multiplier;
    private static final int num_tags = 10*multiplier;
    private static int num_grps = 0; //contador, no editar
    private static int num_evts = 0; //contador, no editar
    //maxims
    private static final int max_grps_x_cm =  10*multiplier;
    private static final int max_evts_x_grp = 10*multiplier;
    private static final int max_tags_x_usr = Math.min(10*multiplier, num_tags);
    private static final int max_tags_x_grp = Math.min(10*multiplier, num_tags);
    //valors relacions
    private static final int max_users_seguits = Math.min(10,num_users);
    private static final int max_grps_seguits = 10*multiplier; //Math.min(10,num_grps). A calcular en ejecucion
    private static final int max_evts_assists = 20*multiplier; //Math.min(10,num_evts). A calcular en ejecucion
    //llistes
    private static ArrayList<Usuario> cms = new ArrayList<Usuario>(num_cms);
    private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>(num_users);
    private static ArrayList<Tag> tags = new ArrayList<Tag>(num_tags);
    private static ArrayList<Grupo> grupos = new ArrayList<Grupo>();
    private static ArrayList<Evento> eventos = new ArrayList<Evento>();
    //llistes strings
    private static ArrayList<String> nicknames = Nicknames.getNicknames();
    private static ArrayList<String> groupnames = Groupnames.getGroupNames();
    private static ArrayList<String> tagnames = TagNames.getTagNames();
    private static ArrayList<String> eventnames = Eventnames.getEventnames();

    private static void generacio_joc_proves() {
        //creem num_tags tags
        for (int i = 0; i < tags.size(); i++) {
            Tag t = tags.get(i);
            long idLong = System.currentTimeMillis();
            t.id = "tag" + i + "-" + String.valueOf(idLong);
            t.nombreTag = tagnames.get(i % tagnames.size()) + getSufix(i, tagnames.size());
        }

        //creem num_cms cms
        for (int i=0; i<cms.size(); i++) {
            Usuario c = cms.get(i);
            c.cm = true;
            c.email = "cm"+i+"@pickanevent.com";
            c.password = "pickanevent";
            c.nickname = "cm"+i;
            c.username = "cm"+i;
            c.bio = "Esta es la descripcion de cm"+i;
            long idLong = System.currentTimeMillis();
            c.id = "cm" + i + "-" + String.valueOf(idLong);
            //cada cm crea una quantitat de grups
            for (int j=0; j<(i%max_grps_x_cm); j++) {
                Grupo g = new Grupo();
                g.descripcion = "Esta es la descripcion del grupo creado por cm"+i;
                g.nombreGrupo = groupnames.get((j+i)%groupnames.size());
                g.idUsuario = c.id;
                g.nickname = c.nickname;
                g.id = "grp" + num_grps + "-" + String.valueOf(idLong);
                //cada group afegeix una quantitat de tags
                for (int k=0; k<(j%max_tags_x_grp); k++) {
                    String idTag = tags.get(k).id;
                    String nombreTag = tags.get(k).nombreTag;
                    g.idTags.put(idTag, nombreTag);
                    Tag t = getTagById(idTag);
                    t.idGrupos.put(g.id, g.nombreGrupo);
                }
                //cada group crea una quantitat de events
                for (int k=0; k<(j%max_evts_x_grp); k++) {
                    Evento e = new Evento();
                    e.id = "Evt" + num_evts + "-" + String.valueOf(idLong);
                    e.descripcion = "Esta es la descripcion del evento " + num_evts;
                    e.titulo = eventnames.get(k%eventnames.size()) + getSufix(i, eventnames.size());
                    e.latitud = "41.361585";
                    e.longitud = "2.1507653";
                    e.webpage = "www." +  e.titulo + ".com";
                    e.precio = String.valueOf(11*k);
                    e.localizacion = "C/Falsa 123, Springfield, Colorado";
                    Date[] dates = generateDates(k);
                    e.dataInici = String.valueOf(dates[0]);
                    e.dataFinal = String.valueOf(dates[1]);
                    eventos.add(e);
                    ++num_evts;
                    g.idEventos.put(e.id, e.titulo);
                    c.idEventos.put(e.id, e.titulo);
                }
                grupos.add(g);
                ++num_grps;
            }
        }

        //creem num_users users
        for (int i=0; i<num_users; i++) {
            Usuario u = usuarios.get(i);
            long idLong = System.currentTimeMillis();
            u.id = "usr" + i + "-" + String.valueOf(idLong);
            u.email = "usr" + i + "@pickanevent.com";
            u.password = "pickanevent";
            u.username = "usr" + i;
            u.nickname = nicknames.get(i%nicknames.size()) + getSufix(i, nicknames.size());
            u.cm = false;
            u.bio = "Esta es la bio-descripcion del usuario usr"+i;
            //cada usuario se añade unos cuantos tags
            for (int j=0; j<(i%max_tags_x_usr); j++) {
                String idTag = tags.get(j).id;
                String nombreTag = tags.get(j).nombreTag;
                u.idTags.put(idTag, nombreTag);
                Tag t = getTagById(idTag);
                t.idUsuarios.put(u.id, u.nickname);
            }
            //cada usuario sigue a unos cuantos grupos
            int mgs = Math.min(max_grps_seguits,num_grps);
            for (int j=0; j<(i%mgs); j++) {
                String idGrupo = grupos.get(j).id;
                String nombreGrupo = grupos.get(j).nombreGrupo;
                u.idGrupos.put(idGrupo, nombreGrupo);
            }
            //cada usuario asiste o NO asiste unos cuantos eventos
            int mea = Math.min(max_evts_assists, num_evts);
            for (int j=0; j<(i%mea); j++) {
                String idEvento = eventos.get(j).id;
                String tituloEvento = eventos.get(j).titulo;
                if (j%2 == 0)
                    u.idEventos.put(idEvento, tituloEvento);
                else
                    u.idEventosNo.put(idEvento, tituloEvento);
            }
            //cada usuario sigue a unos cuantos usuarios
            int mus = Math.min(max_users_seguits, num_users);
            for (int j=0; j<(i%mus); j++) {
                if (j!=i) {
                    String idUsuario = usuarios.get(j).id;
                    String nickname = usuarios.get(j).nickname;
                    u.idUsuarios.put(idUsuario, nickname);
                }
            }
        }
    }

    private static void printJson() {
        System.out.println("{");
        System.out.println("\"Usuarios\": {");
        for (Usuario u : usuarios) {
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            System.out.println("\"email\": " + "\"" + u.email + "\",");
            System.out.println("\"password\": " + "\"" + u.password + "\",");
            System.out.println("\"username\": " + "\"" + u.username + "\",");
            System.out.println("\"nickname\": " + "\"" + u.nickname + "\",");
            System.out.println("\"bio\": " + "\"" + u.bio + "\",");
            System.out.println("\"cm\": " + String.valueOf(u.cm) + ",");
            if (u.idUsuarios.size() > 0) {
                System.out.println("\"idUsuarios\": {");
                for (String key : u.idUsuarios.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idUsuarios.get(key) + "\",");
                System.out.println("},");
            }
            if (u.idGrupos.size() > 0) {
                System.out.println("\"idGrupos\": {");
                for (String key : u.idGrupos.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idGrupos.get(key) + "\",");
                System.out.println("},");
            }
            if (u.idEventos.size() > 0) {
                System.out.println("\"idEventos\": {");
                for (String key : u.idEventos.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idEventos.get(key) + "\",");
                System.out.println("},");
            }
            if (u.idEventosNo.size() > 0) {
                System.out.println("\"idEventosNo\": {");
                for (String key : u.idEventosNo.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idEventosNo.get(key) + "\",");
                System.out.println("},");
            }
            if (u.idTags.size() > 0) {
                System.out.println("\"idTags\": {");
                for (String key : u.idTags.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idTags.get(key) + "\",");
                System.out.println("},");
            }
        }
        System.out.println("},"); //hem acabat els usuaris
        System.out.println("\"Grupos\": {");
        for (Grupo u : grupos) {
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            System.out.println("\"nombreGrupo\": " + "\"" + u.nombreGrupo + "\",");
            System.out.println("\"descripcion\": " + "\"" + u.descripcion + "\",");
            System.out.println("\"nickname\": " + "\"" + u.nickname + "\",");
            System.out.println("\"idUsuario\": " + "\"" + u.idUsuario + "\",");
            if (u.idTags.size() > 0) {
                System.out.println("\"idTags\": {");
                for (String key : u.idTags.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idTags.get(key) + "\",");
                System.out.println("},");
            }
            if (u.idEventos.size() > 0) {
                System.out.println("\"idEventos\": {");
                for (String key : u.idEventos.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idEventos.get(key) + "\",");
                System.out.println("},");
            }
        }
        System.out.println("},"); //hem acabat els grups
        System.out.println("\"Tags\": {");
        for (Tag u : tags) {
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            System.out.println("\"nombreTag\": " + "\"" + u.nombreTag + "\",");
            if (u.idUsuarios.size() > 0) {
                System.out.println("\"idUsuarios\": {");
                for (String key : u.idUsuarios.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idUsuarios.get(key) + "\",");
                System.out.println("},");
            }
            if (u.idGrupos.size() > 0) {
                System.out.println("\"idGrupos\": {");
                for (String key : u.idGrupos.keySet())
                    System.out.println("\"" + key + "\": \"" + u.idGrupos.get(key) + "\",");
                System.out.println("},");
            }
        }
        System.out.println("},"); //hem acabat els tags
        System.out.println("\"Eventos\": {");
        for (Evento u : eventos) {
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            System.out.println("\"titulo\": " + "\"" + u.titulo + "\",");
            System.out.println("\"descripcion\": " + "\"" + u.descripcion + "\",");
            System.out.println("\"localizacion\": " + "\"" + u.localizacion + "\",");
            System.out.println("\"dataInici\": " + "\"" + u.dataInici + "\",");
            System.out.println("\"dataFinal\": " + "\"" + u.dataFinal + "\",");
            System.out.println("\"precio\": " + "\"" + u.precio + "\",");
            System.out.println("\"webpage\": " + "\"" + u.webpage + "\",");
            System.out.println("\"latitud\": " + "\"" + u.latitud + "\",");
            System.out.println("\"longitud\": " + "\"" + u.longitud + "\"");
        }
        System.out.println("},"); //hem acabat els events
    }

    //class helpers to make the list
    public static class Evento {
        public String id;
        public String descripcion;
        public String titulo;
        public String webpage;
        public String precio;
        public String latitud;
        public String longitud;
        public String imagen;
        public String localizacion;
        public String dataInici;
        public String dataFinal;
    }

    public static class Usuario {
        public String id;
        public String email;
        public String username;
        public String nickname;
        public String password;
        public String bio;
        public String imagen;
        public Boolean cm;
        HashMap<String,String> idEventos; //key: idEvt, value: tituloEvt
        HashMap<String,String> idEventosNo; //key: idEvt, value: tituloEvt
        HashMap<String,String> idGrupos; //key: idEvt, value: nombreGrupo
        HashMap<String,String> idTags; //key: idEvt, value: nombreTag
        HashMap<String,String> idUsuarios; //key: idEvt, value: tituloEvt
    }

    public static class Grupo {
        public String id;
        public String descripcion;
        public String idUsuario;
        public String imagen;
        public String nickname;
        public String nombreGrupo;
        HashMap<String,String> idEventos; //key: idEvt, value: tituloEvt
        HashMap<String,String> idTags; //key: idTag, value: nombreTag
    }

    public static class Tag {
        public String id;
        public String nombreTag;
        HashMap<String,String> idUsuarios; //key: idUsr, value: nickname
        HashMap<String,String> idGrupos; //key: idGrp, value: nombreGrupo
    }

    //helping functions
    private static Date[] generateDates(int j) {
        //generem unes dates
        int year = 2016 + j;
        int month = j%11;
        int day = ((j+29)%29)+1;
        int hour = j%24;
        int min = (j%4)*15;
        boolean sameDay = (j%2 == 0)? true : false;
        Date dataInici = new Date(year-1900, month, day, hour, min);
        Date dataFinal;
        if (sameDay) {
            hour += 4;
            if (hour>24) {
                hour%=24;
                day++;
            }
            dataFinal = new Date(year-1900, month, day, hour, min);
        }
        else {
            day += (j+1);
            if (day>30) {
                day%=30;
                month++;
            }
            if (month>11) {
                month%=11;
                year++;
            }
            dataFinal = new Date(year-1900, month, day, hour, min);
        }
        Date[] dates = {dataInici, dataFinal};
        return dates;
    }

    public static class Nicknames {
        public static ArrayList<String> llista_nicknames = new ArrayList<String>();

        public Nicknames() {
            llista_nicknames.add("Deadpool");
            llista_nicknames.add("IronMan");
            llista_nicknames.add("Thor");
            llista_nicknames.add("Hulk");
            llista_nicknames.add("Antman");
            llista_nicknames.add("CapitanAmerica");
            llista_nicknames.add("DrStrange");
            llista_nicknames.add("Batman");
            llista_nicknames.add("Superman");
            llista_nicknames.add("WonderWoman");
            llista_nicknames.add("Aquaman");
            llista_nicknames.add("Flash");
            llista_nicknames.add("Punisher");
            llista_nicknames.add("DareDevil");
            llista_nicknames.add("Spiderman");
            llista_nicknames.add("LexLuthor");
            llista_nicknames.add("Joker");
            llista_nicknames.add("Bane");
            llista_nicknames.add("Catwoman");
            llista_nicknames.add("StarLord");
            llista_nicknames.add("Thanos");
            llista_nicknames.add("Goku");
            llista_nicknames.add("Vegeta");
            llista_nicknames.add("Trunks");
            llista_nicknames.add("Celula");
            llista_nicknames.add("CorPetit");
            llista_nicknames.add("Gohan");
            llista_nicknames.add("Krillin");
            llista_nicknames.add("Buu");
            llista_nicknames.add("Freezer");
            llista_nicknames.add("Sean Connery");
            llista_nicknames.add("DeadShot");
            llista_nicknames.add("HarleyQuinn");
            llista_nicknames.add("LaCosa");
            llista_nicknames.add("InvisibleWoman");
            llista_nicknames.add("MrFantastic");
            llista_nicknames.add("HumanTorch");
            llista_nicknames.add("CharlesXavier");
            llista_nicknames.add("Wolverine");
        }

        public static ArrayList<String> getNicknames() {
            return llista_nicknames;
        }
    }

    public static class Groupnames {
        public static ArrayList<String> llista_groupnames = new ArrayList<String>();

        public Groupnames() {
            llista_groupnames.add("Los Vengadores");
            llista_groupnames.add("Villanos");
            llista_groupnames.add("Justicieros");
            llista_groupnames.add("Liga de la Justicia");
            llista_groupnames.add("X-Men");
            llista_groupnames.add("Inhumans");
            llista_groupnames.add("Super Saiyans");
            llista_groupnames.add("La Liga de los hombres extraordinarios");
            llista_groupnames.add("Suicide Squad");
            llista_groupnames.add("Los 4 Fantásticos");
        }

        public static ArrayList<String> getGroupNames() {
            return llista_groupnames;
        }
    }

    public static class TagNames {
        public static ArrayList<String> llista_tagnames = new ArrayList<String>();

        public TagNames() {
            llista_tagnames.add("Venganza");
            llista_tagnames.add("Justicia");
            llista_tagnames.add("Maldad");
            llista_tagnames.add("Bondad");
            llista_tagnames.add("Indiferencia");
            llista_tagnames.add("Diversion");
            llista_tagnames.add("Poder");
            llista_tagnames.add("Locura");
            llista_tagnames.add("Anarquia");
            llista_tagnames.add("Purga");
        }

        public static ArrayList<String> getTagNames() {
            return llista_tagnames;
        }
    }

    public static class Eventnames {
        public static ArrayList<String> llista_eventnames = new ArrayList<String>();

        public Eventnames() {
            llista_eventnames.add("Atraco a un banco");
            llista_eventnames.add("Conquista del mundo");
            llista_eventnames.add("Conquista de la galaxia");
            llista_eventnames.add("Atraco a un geriatrico");
            llista_eventnames.add("Ataque a los superheroes");
            llista_eventnames.add("Ataque a los villanos");
            llista_eventnames.add("Defender a la poblacion");
            llista_eventnames.add("Defender la guarida");
            llista_eventnames.add("Ir de copas");
            llista_eventnames.add("Ir de putas");
            llista_eventnames.add("Aprobar PES");
        }

        public static ArrayList<String> getEventnames() {
            return llista_eventnames;
        }
    }

    private static String getSufix(int i, int size) {
        String suf = "";
        if (i >= size) {
            int div = i/size + 1;
            suf = "" + div;
        }
        return suf;
    }

    private static Tag getTagById(String id) {
        for (Tag t : tags) {
            if (t.id.equals(id)) {
                return t;
            }
        }
        return null;
    }

}
