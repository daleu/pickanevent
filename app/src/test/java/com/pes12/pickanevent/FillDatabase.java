package com.pes12.pickanevent;

import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * Created by p4triot on 10/11/2016.
 */

public class FillDatabase {

    private static final int multiplier = 1;
    //valors
    private static final int num_usuaris = 100*multiplier;
    private static final int num_grups = 20*multiplier;

    private static final int num_tags = 10*multiplier;
    private static final int max_tags_per_event = 3*(multiplier/3);
    private static final int max_tags_per_group = 3*(multiplier/3);

    private static final int max_evs_per_grup = 10*multiplier; //els grups tindran entre 0 i 10 events
    private static final int max_users_seguits = (num_usuaris/2)*multiplier; //els usuaris seguiran entre 0 i 50 usuaris
    private static final int max_groups_seguits = (num_grups/2)*multiplier; //els usuaris seguiran com a molt entre 0 i 10 grups
    private static final int max_event_asistencia = ((num_grups*max_evs_per_grup)/4)*multiplier;

    private ArrayList<String> ids_cms = new ArrayList<>();
    private ArrayList<String> ids_users = new ArrayList<>();
    private ArrayList<String> ids_tags = new ArrayList<>();
    private ArrayList<String> ids_grups = new ArrayList<>();
    private ArrayList<String> ids_events = new ArrayList<>();
    private ArrayList<String> developer_ids = new ArrayList<>();

    //creacio usuaris: nickname i psw son el mateix
    private void creacio_usuaris() {
        ArrayList<String> nicknames = Nicknames.getNicknames();
        for (int i=0; i<num_usuaris; ++i) {
            boolean cm = (i%10 == 0)? true : false; //uno de cada 10 es CM
            String suffix = (i>nicknames.size())? Integer.toString(i) : "";
            String nickname = nicknames.get(i%(nicknames.size())) + suffix;
            UsuarioEntity temp_usr = new UsuarioEntity(nickname, cm) ;
            String email = nickname + "@pickanevent.upc";
            String password = nickname;
            String userId = Insertar_usuari(temp_usr, email, password);
            if (cm)
                ids_cms.add(userId);
            else
                ids_users.add(userId);
        }

        //Afegim un compte per cadascun dels membres del grup
        String[] developers = {"Victor", "Clara", "Oscar", "David", "Jordi", "Edgar", "Jan"};
        for (String dev : developers)
            developer_ids.add(Insertar_usuari(new UsuarioEntity(dev, false), dev + "@pickevent.upc", dev));
    }

    private void creacio_tags() {
        ArrayList<String> tagnames = TagNames.getTagNames();
        for (int i=0; i<num_tags; ++i) {
            String suffix = (i>tagnames.size())? Integer.toString(i) : "";
            String tagname = tagnames.get(i%(tagnames.size())) + suffix;
            TagEntity temp_tag = new TagEntity(tagname) ;
            String idTag = Insertar_tag(temp_tag);
            ids_tags.add(idTag);
        }
    }

    private void creacio_grups() {
        ArrayList<String> groupnames = Groupnames.getGroupNames();
        for (int i=0; i<num_grups; ++i) {
            String suffix = (i>groupnames.size())? Integer.toString(i) : "";
            String groupname = groupnames.get(i%(groupnames.size())) + suffix;
            String[] separated = groupname.split(" ");
            String webpage = "www." + separated[0] + suffix + ".com";
            String descripcio = "Esta es la descripcio para el grupo " + groupname + ".";
            GrupoEntity temp_grp = new GrupoEntity();
            temp_grp.setNombreGrupo(groupname);
            temp_grp.setDescripcion(descripcio);
            temp_grp.setWebpage(webpage);
            String idCreador = ids_cms.get(i%ids_cms.size());
            int num_tags = i%max_tags_per_group;
            String idGrup = Insertar_grup(temp_grp, idCreador, num_tags);
            ids_grups.add(idGrup);
        }
    }

    private void creacio_events() {
        ArrayList<String> eventnames = Eventnames.getEventnames();
        for (int i=0; i<ids_grups.size(); ++i){
            String idGrupCreador = ids_grups.get(i);
            for (int j=0; j<(i%max_evs_per_grup); ++j) {
                String eventname = eventnames.get(j%(eventnames.size()));
                EventoEntity temp_evt = new EventoEntity();
                temp_evt.setTitulo(eventname);
                temp_evt.setDescripcion("Esta es la descripcion para el evento " + eventname + " del grupo con id " + idGrupCreador);
                temp_evt.setPrecio(Integer.toString((i+j)*10) + "€");
                String[] separated = eventname.split(" ");
                temp_evt.setWebpage("www." + separated[0] + ".com");
                temp_evt.setLocalizacion("Av Polo Norte 41");
                Date[] dates = generateDates(j);
                temp_evt.setDataInici(dates[0]);
                temp_evt.setDataFinal(dates[1]);
                temp_evt.setLongitud(Integer.toString((i+j+1)*10));
                temp_evt.setLatitud(Integer.toString(abs(j+i+-1)*10));
                int num_tags = j%max_tags_per_event;
                String idEvent = Insertar_event(temp_evt, idGrupCreador, num_tags);
                ids_events.add(idEvent);
            }
        }
    }

    private void generacio_seguiments_usuaris() {
        for (int j = 0; j < ids_users.size(); ++j) {
            String idUsuari = ids_users.get(j);
            int max_seguits = ((j*12345689) % max_users_seguits ) % ids_users.size();
            for (int i = 0; i < max_seguits; ++i) {
                int seguir = (i + j) % ids_users.size();
                if (i != j)
                    Insertar_seguimentUsuari(idUsuari, ids_users.get(seguir));
            }
        }
    }

    private void generacio_seguiments_grups() {
        for (int j = 0; j < ids_users.size(); ++j) {
            String idUsuari = ids_users.get(j);
            int max_seguits = ((j*12345689) % max_groups_seguits ) % ids_grups.size();
            for (int i = 0; i < max_seguits; ++i) {
                int seguir = (i + j) % ids_grups.size();
                Insertar_seguimentUsuari(idUsuari, ids_grups.get(seguir));
            }
        }
    }

    private void generacio_asistencies() {
        for (int j = 0; j < ids_users.size(); ++j) {
            String idUsuari = ids_users.get(j);
            int max_assistits = ((j*12345689) % max_event_asistencia) % ids_events.size();
            for (int i = 0; i < max_assistits; ++i) {
                int assistir = (i + j) % ids_events.size();
                if (i%2 == 0)
                    Insertar_asistenciaEvent(idUsuari, ids_events.get(assistir));
                else
                    Insertar_noAsistenciaEvent(idUsuari, ids_events.get(assistir));
            }
        }
    }

    private String Insertar_usuari(UsuarioEntity usr, String email, String psw) {
        //retornar idUsuari
        return "";
    }

    private String Insertar_tag(TagEntity tag) {
        //retornar idTag
        return "";
    }

    private String Insertar_grup(GrupoEntity grp, String idCreador, int num_tags) {
        //redundancia de dades de tags del grup a GRUPS i de grups del tag a TAGS
        //redundancia de dades de grups creats al CM i de creador al GRUP
        //retornar idGrup
        return "";
    }

    private String Insertar_event(EventoEntity evt, String idGrupCreador, int num_tags) {
        //redundancia de dades de tags del event a EVENTS i de events del tag a TAGS
        //retornar idEvent
        return "";
    }

    private void Insertar_seguimentUsuari(String idSeguidor, String idSeguit) {
    }

    private void Insertar_asistenciaEvent(String idAsistidor, String idEvent) {
        //redundancia de dades de usuari i events. Cal inserir a tots dos.
    }

    private void Insertar_noAsistenciaEvent(String idAsistidor, String idEvent) {
        //redundancia de dades de usuari i events. Cal inserir a tots dos.
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

    private Date[] generateDates(int j) {
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
}
