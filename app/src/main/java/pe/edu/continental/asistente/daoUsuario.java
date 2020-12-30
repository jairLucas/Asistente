package pe.edu.continental.asistente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pe.edu.continental.asistente.Entidades.Usuario;

public class daoUsuario {
    Context c;
    Usuario u;
    ArrayList<Usuario> lista;
    SQLiteDatabase sql;
    String bd="BDDaug";
    String tabla= "create table if not exists usuario(id integer primary key autoincrement, usuario text, pass text, nombre text, ap text)";

    public daoUsuario(Context c){
        this.c=c;
        sql = c.openOrCreateDatabase(bd,c.MODE_PRIVATE,null); //abre y cierra bd
        sql.execSQL(tabla);
        u=new Usuario();
    }
    public boolean insertUsuario(Usuario u){
        if (buscar(u.getUsuario())==0){
            ContentValues cv = new ContentValues();
            cv.put("usuario",u.getUsuario());
            cv.put("pass",u.getPass());
            cv.put("nombre",u.getNombre());
            cv.put("ap",u.getApellidos());
            return (sql.insert("usuario",null,cv)>0);

        }else {

            return false;
        }
    }

    public int buscar(String u){
        int x=0;
        lista=selectUsuario();
        for (Usuario us:lista) {//si encuentra un usuario repetido
            if (us.getUsuario().equals(u))

                x++;
        }
        return x; //regresa 1 dentro de x

    }

    public ArrayList<Usuario> selectUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        lista.clear();
        Cursor cr=sql.rawQuery("select*from usuario",null);
        if(cr!=null&&cr.moveToFirst()){
            do {
                Usuario u = new Usuario();
                u.setUsuario(cr.getString(1));
                u.setPass(cr.getString(2));
                u.setNombre(cr.getString(3));
                u.setApellidos(cr.getString(4));
                lista.add(u);
            }while (cr.moveToNext());
        }
        return lista;
    }

    public int login(String u, String p){
        int a =0;

        Cursor cr=sql.rawQuery("select*from usuario",null);
        if(cr!=null&&cr.moveToFirst()){
            do {
                if(cr.getString(1).equals(u)&&cr.getString(2).equals(p)){
                    a++;
                }

            }while (cr.moveToNext());
    }
        return a;
    }
    //metodo si inicio
    public Usuario getUsuario(String u, String p){
        lista=selectUsuario();
        for (Usuario us:lista){
            if (us.getUsuario().equals(u)&&us.getPass().equals(p)){
                return us;
            }
        }
        return null;
    }
    //metodo para buscar po id
    

    public Usuario getUsuarioById(int id){
        lista=selectUsuario();
        for(Usuario us:lista){
            if(us.getId()==id){
                return us;
            }
        }
        return null;
    }
}
