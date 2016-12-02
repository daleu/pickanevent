import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by p4triot on 28/11/2016.
 */

public class GenerateJocProves {


    public static void main(String[] args) {
        generacio_hardcoded();
        generacio_joc_proves();
        printJson();
    }

    private static final int multiplier = 10;
    //valors
    private static final int num_users = 10*multiplier;
    private static final int num_cms = (multiplier>1)? 1*multiplier : 2;
    private static final int num_tags = (multiplier>1)? 1*multiplier : 2;
    private static int num_grps = 0; //contador, no editar
    private static int num_evts = 0; //contador, no editar
    //maxims
    private static final int max_grps_x_cm =  (multiplier>1)? 1*multiplier : 2;
    private static final int max_evts_x_grp = (multiplier>1)? 1*multiplier : 2;
    private static final int max_tags_x_usr = Math.min(10*multiplier, num_tags);
    private static final int max_tags_x_grp = Math.min(10*multiplier, num_tags);
    //valors relacions
    private static final int max_users_seguits = Math.min(1*multiplier,num_users);
    private static final int max_grps_seguits = (multiplier>1)? 1*multiplier : 2; //Math.min(10,num_grps). A calcular en ejecucion
    private static final int max_evts_assists = 2*multiplier; //Math.min(10,num_evts). A calcular en ejecucion
    //llistes
    private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>(num_users+num_cms);
    private static ArrayList<Usuario> harcodedUsers = new ArrayList<Usuario>();
    private static ArrayList<Tag> tags = new ArrayList<Tag>(num_tags);
    private static ArrayList<Grupo> grupos = new ArrayList<Grupo>();
    private static ArrayList<Evento> eventos = new ArrayList<Evento>();
    //llistes strings
    private static final Nicknames nicksList = new Nicknames();
    private static final Groupnames groupsList = new Groupnames();
    private static final TagNames tagsList = new TagNames();
    private static final Eventnames eventsList = new Eventnames();
    private static ArrayList<String> nicknames = nicksList.getNicknames();
    private static ArrayList<String> groupnames = groupsList.getGroupNames();
    private static ArrayList<String> tagnames = tagsList.getTagNames();
    private static ArrayList<String> eventnames = eventsList.getEventnames();

    private static Usuario createU(String id, String nickname, Boolean cm) {
        Usuario u = new Usuario();
        u.id = id;
        u.nickname = nickname;
        u.cm = cm;
        return u;
    }

    private static void generacio_hardcoded() {
        ArrayList<Usuario> l = harcodedUsers;
        l.add(createU("2u9PWFE9uIhJTLVWfMW0wFH8Axt1", "p4triot", false));
        l.add(createU("2UFk5r02iuRfuINh4jASrwnmFik2", "Oscar", true));
        l.add(createU("GK87HBOwSkTmC12MjzZz16ghYXr2", "claraaaa", false));
        l.add(createU("PtYUmiUYA3S83ohzYmarVCACBeG2", "claraCM", true));
        l.add(createU("Qch3yrsCXwgyL4os8ujikGVaNwW2", "Aleueet", false));
        l.add(createU("S8P83FZ1RLabdWFODCVZmUajWBs1", "PES", false));
        l.add(createU("VSDWkL1WEqPEnAwSpTs8Oxi3WXz1", "leg", false));
        l.add(createU("gIHcRzhypsY0tWkqjvIWkMNcEmZ2", "nuevotest", false));
        l.add(createU("haIMoULYKRhynBStMtfQl2c9SxC2", "pop", false));
        l.add(createU("lvKSWbg21Qgye7CGsqIXtdSsGck2", "lega", false));
        l.add(createU("lvKSWbg21Qgye7CGsqIXtdSsGck2", "z", false));
    }

    private static void generacio_joc_proves() {
        //creem num_tags tags
        for (int i = 0; i < num_tags; i++) {
            Tag t = new Tag();
            tags.add(t);
            long idLong = System.currentTimeMillis();
            t.id = "tag" + i + "-" + String.valueOf(idLong);
            t.nombreTag = tagnames.get(i % tagnames.size()) + getSufix(i, tagnames.size());
        }

        //creem num_cms cms
        for (int i=0; i<num_cms; i++) {
            Usuario c = new Usuario();
            usuarios.add(c);
            c.cm = true;
            c.email = "cm"+i+"@pickanevent.com";
            c.password = "pickanevent";
            c.nickname = "cm"+i;
            c.username = "cm"+i;
            c.bio = "Esta es la descripcion de cm"+i;
            if (i%2 == 0) c.bio = null;
            long idLong = System.currentTimeMillis();
            c.id = "cm" + i + "-" + String.valueOf(idLong);
            //cada cm crea una quantitat de grups
            for (int j=0; j<(i%max_grps_x_cm); j++) {
                Grupo g = new Grupo();
                grupos.add(g);
                g.descripcion = "Esta es la descripcion del grupo creado por cm"+i;
                if (j%5 == 0) g.descripcion = null;
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
                    eventos.add(e);
                    e.id = "Evt" + num_evts + "-" + String.valueOf(idLong);
                    e.descripcion = "Esta es la descripcion del evento " + num_evts;
                    if (k%5 == 0) e.descripcion = null;
                    e.titulo = eventnames.get(k%eventnames.size()) + getSufix(i, eventnames.size());
                    e.latitud = "41.361585";
                    e.longitud = "2.1507653";
                    e.localizacion = "C/Falsa 123, Springfield, Colorado";
                    if (k%4 == 0) {e.latitud = null; e.longitud = null; e.localizacion = null;}
                    String separated[] = e.titulo.split(" ");
                    e.webpage = "www." +  separated[0] + getSufix(i, eventnames.size()) + ".com";
                    if (k%3 == 0) e.webpage = null;
                    e.precio = String.valueOf(11*k);
                    if (k%2 == 0) e.precio = null;
                    e.idGrup = g.id;
                    Date[] dates = generateDates(k);
                    e.dataInici = dates[0].getTime();
                    e.dataFinal = dates[1].getTime();
                    if (k%4 == 1) e.dataFinal = null;
                    ++num_evts;
                    g.idEventos.put(e.id, e.titulo);
                    c.idEventos.put(e.id, e.titulo);
                }
                c.idGrupos.put(g.id, g.nombreGrupo);
                ++num_grps;
            }
        }

        //creem num_users users
        for (int i=0; i<num_users; i++) {
            Usuario u = new Usuario();
            usuarios.add(u);
            long idLong = System.currentTimeMillis();
            u.id = "usr" + i + "-" + String.valueOf(idLong);
            u.email = "usr" + i + "@pickanevent.com";
            u.password = "pickanevent";
            u.username = "usr" + i;
            u.nickname = nicknames.get(i%nicknames.size()) + getSufix(i, nicknames.size());
            u.cm = false;
            if (i%3 == 0) u.cm = null;
            u.bio = "Esta es la bio-descripcion del usuario usr"+i;
            if (i%2 == 0) u.bio = null;
            //cada usuario se añade unos cuantos tags
            for (int j=0; j<(i%max_tags_x_usr); j++) {
                String idTag = tags.get(j).id;
                String nombreTag = tags.get(j).nombreTag;
                u.idTags.put(idTag, nombreTag);
            }
            //cada usuario sigue a unos cuantos grupos
            int mgs = Math.min(max_grps_seguits,num_grps);
            for (int j=0; mgs>0 && j<(i%mgs); j++) {
                String idGrupo = grupos.get(j).id;
                String nombreGrupo = grupos.get(j).nombreGrupo;
                u.idGrupos.put(idGrupo, nombreGrupo);
            }
            //cada usuario asiste o NO asiste unos cuantos eventos
            int mea = Math.min(max_evts_assists, num_evts);
            for (int j=0; mea>0 && j<(i%mea); j++) {
                String idEvento = eventos.get(j).id;
                String tituloEvento = eventos.get(j).titulo;
                u.idEventos.put(idEvento, tituloEvento);
            }
            //cada usuario sigue a unos cuantos usuarios
            int mus = Math.min(max_users_seguits, num_users);
            for (int j=0; j<(i%mus); j++) {
                if (j!=i) {
                    int userPos = (j+num_cms)%num_users;
                    Usuario aux = usuarios.get(userPos);
                    String idUsuario = aux.id;
                    String nickname = aux.nickname;
                    u.idUsuarios.put(idUsuario, nickname);
                }
            }
        }
    }

    private static void printJson() {
        System.out.println("{");
        System.out.println("\"usuarios\": {");
        boolean first = true;
        boolean first2 = true;
        usuarios.addAll(harcodedUsers);
        for (Usuario u : usuarios) {
            if (!first)
                System.out.print(",");
            else
                first = false;
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            if (u.email != null)
                System.out.println(",\"email\": " + "\"" + u.email + "\"");
            if (u.password != null)
                System.out.println(",\"password\": " + "\"" + u.password + "\"");
            if (u.username != null)
                System.out.println(",\"username\": " + "\"" + u.username + "\"");
            System.out.println(",\"nickname\": " + "\"" + u.nickname + "\"");
            if (u.bio != null)
                System.out.println(",\"bio\": " + "\"" + u.bio + "\"");
            if (u.cm != null)
                System.out.println(",\"cm\": " + String.valueOf(u.cm));
            if (u.idUsuarios.size() > 0) {
                System.out.print(",");
                System.out.println("\"idUsuarios\": {");
                first2 = true;
                for (String key : u.idUsuarios.keySet()) {
                    if (!first2){ System.out.print(","); }else{ first2 = false;}
                    System.out.println("\"" + key + "\": \"" + u.idUsuarios.get(key) + "\"");
                }
                System.out.println("}");
            }
            if (u.idGrupos.size() > 0) {
                System.out.print(",");
                System.out.println("\"idGrupos\": {");
                first2 = true;
                for (String key : u.idGrupos.keySet()){
                    if (!first2) System.out.print(","); else first2 = false;
                    System.out.println("\"" + key + "\": \"" + u.idGrupos.get(key) + "\"");
                }
                System.out.println("}");
            }
            if (u.idEventos.size() > 0) {
                System.out.print(",");
                System.out.println("\"idEventos\": {");
                first2 = true;
                for (String key : u.idEventos.keySet()){
                    if (!first2) System.out.print(","); else first2 = false;
                    System.out.println("\"" + key + "\": \"" + u.idEventos.get(key) + "\"");
                }
                System.out.println("}");
            }
            if (u.idTags.size() > 0) {
                System.out.print(",");
                System.out.println("\"idTags\": {");
                first2 = true;
                for (String key : u.idTags.keySet()) {
                    if (!first2) System.out.print(","); else first2 = false;
                    System.out.println("\"" + key + "\": \"" + u.idTags.get(key) + "\"");
                }
                System.out.println("}");
            }
            System.out.println("}");
        }
        System.out.println("},"); //hem acabat els usuaris
        System.out.println("\"grupos\": {");
        first = true;
        for (Grupo u : grupos) {
            if (!first)
                System.out.print(",");
            else
                first = false;
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            System.out.println(",\"nombreGrupo\": " + "\"" + u.nombreGrupo + "\"");
            if (u.descripcion != null)
                System.out.println(",\"descripcion\": " + "\"" + u.descripcion + "\"");
            System.out.println(",\"nickname\": " + "\"" + u.nickname + "\"");
            System.out.println(",\"idUsuario\": " + "\"" + u.idUsuario + "\"");
            if (u.idTags.size() > 0) {
                System.out.println(",");
                System.out.println("\"idTags\": {");
                first2 = true;
                for (String key : u.idTags.keySet()) {
                    if (!first2) System.out.print(","); else first2 = false;
                    System.out.println("\"" + key + "\": \"" + u.idTags.get(key) + "\"");
                }
                System.out.println("}");
            }
            if (u.idEventos.size() > 0) {
                System.out.println(",");
                System.out.println("\"idEventos\": {");
                first2 = true;
                for (String key : u.idEventos.keySet()) {
                    if (!first2) System.out.print(","); else first2 = false;
                    System.out.println("\"" + key + "\": \"" + u.idEventos.get(key) + "\"");
                }
                System.out.println("}");
            }
            System.out.println("}");
        }
        System.out.println("},"); //hem acabat els grups
        System.out.println("\"tags\": {");
        first = true;
        for (Tag u : tags) {
            if (!first)
                System.out.print(",");
            else
                first = false;
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\",");
            System.out.println("\"nombreTag\": " + "\"" + u.nombreTag + "\"");
            if (u.idGrupos.size() > 0) {
                System.out.println(",");
                System.out.println("\"idGrupos\": {");
                first2 = true;
                for (String key : u.idGrupos.keySet()) {
                    if (!first2) System.out.print(","); else first2 = false;
                    System.out.println("\"" + key + "\": \"" + u.idGrupos.get(key) + "\"");
                }
                System.out.println("}");
            }
            System.out.println("}");
        }
        System.out.println("},"); //hem acabat els tags
        System.out.println("\"eventos\": {");
        first = true;
        for (Evento u : eventos) {
            if (!first)
                System.out.print(",");
            else
                first = false;
            System.out.println("\"" + u.id + "\" : {");
            System.out.println("\"id\": " + "\"" + u.id + "\"");
            System.out.println(",\"titulo\": " + "\"" + u.titulo + "\"");
            if (u.descripcion != null)
                System.out.println(",\"descripcion\": " + "\"" + u.descripcion + "\"");
            if (u.localizacion != null)
                System.out.println(",\"localizacion\": " + "\"" + u.localizacion + "\"");
            System.out.println(",\"dataInici\": " + "\"" + u.dataInici + "\"");
            if (u.dataFinal != null)
                System.out.println(",\"dataFinal\": " + "\"" + u.dataFinal + "\"");
            if (u.precio != null)
                System.out.println(",\"precio\": " + "\"" + u.precio + "\"");
            if (u.webpage != null)
                System.out.println(",\"webpage\": " + "\"" + u.webpage + "\"");
            if (u.latitud != null)
                System.out.println(",\"latitud\": " + "\"" + u.latitud + "\"");
            if (u.longitud != null)
                System.out.println(",\"longitud\": " + "\"" + u.longitud + "\"");
            System.out.println(",\"idGrup\": " + "\"" + u.idGrup + "\"");
            System.out.println("}");
        }
        System.out.println("}"); //hem acabat els events
        System.out.println("}");
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
        public String localizacion;
        public Long dataInici;
        public Long dataFinal;
        public String idGrup;
    }

    public static class Usuario {
        public String id;
        public String email;
        public String username;
        public String nickname;
        public String password;
        public String bio;
        public Boolean cm;
        HashMap<String,String> idEventos = new HashMap<String,String>(); //key: idEvt, value: tituloEvt
        HashMap<String,String> idGrupos = new HashMap<String,String>(); //key: idEvt, value: nombreGrupo
        HashMap<String,String> idTags = new HashMap<String,String>(); //key: idEvt, value: nombreTag
        HashMap<String,String> idUsuarios = new HashMap<String,String>(); //key: idEvt, value: tituloEvt
    }

    public static class Grupo {
        public String id;
        public String descripcion;
        public String idUsuario;
        public String nickname;
        public String nombreGrupo;
        HashMap<String,String> idEventos = new HashMap<String,String>(); //key: idEvt, value: tituloEvt
        HashMap<String,String> idTags = new HashMap<String,String>(); //key: idTag, value: nombreTag
    }

    public static class Tag {
        public String id;
        public String nombreTag;
        HashMap<String,String> idGrupos = new HashMap<String,String>(); //key: idGrp, value: nombreGrupo
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
