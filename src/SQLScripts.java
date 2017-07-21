import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class SQLScripts {
    static ResultSet getRS_05(Connection conn, String day_begin, String day_end) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String SQL = "SELECT count(*) as vkl_number,vkltyp,kodval,sum(Suma) as suma_vkl, sum(Suma*ProcentSt) as proc_vkl from sbon.vklady " +
                    "where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' group by vkltyp,kodval;";
            rs = statement.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    static ResultSet getRS_06(Connection conn, String day_begin, String day_end) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String SQL = "SELECT round(sum(suma*Kurs_Nbu/Koef)) as suma_vkl,GroupR031,vkltypaggr from sbon.vklady,sbon.dov_val,sbon.kurs_val " +
                    "where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' and vklady.KodVal=dov_val.CodeR030 and vklady.KodVal=kurs_val.KodVal " +
                    "group by GroupR031,VklTypAggr;";
            rs = statement.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    static ResultSet getRS_03(Connection conn, String day_for) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String SQL = "SELECT vkltyp,kodval,sum(Suma) as suma_vkl, sum(Suma*ProcentSt) as proc_vkl from sbon.vklady " +
                    "where date_format(DataOp,\"%d%m%Y\")='" + day_for + "' " +
                    "group by vkltyp,kodval;";
            rs = statement.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    static ResultSet getRS_91(Connection conn, String day_begin, String day_end) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String SQL = "SELECT sum(round(suma*Kurs_Nbu/Koef)) as suma_vkl,vklady.kodval,kodstrok,idn_podatk,client.name,country,resident" +
                    " from sbon.vklady,sbon.client,sbon.kurs_val," +
                    "sbon.dov_vkl where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' and vklady.KodVal=kurs_val.KodVal and vklady.asnumber=client.asnumber and " +
                    "suma>0 group by vklady.asnumber,suma desc;";
            rs = statement.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    static ResultSet getRS_A7(Connection conn, String day_begin, String day_end) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String SQL = "SELECT round(sum(suma*Kurs_Nbu/Koef)) as suma_vkl,GroupR031,kodstrok from sbon.vklady,sbon.dov_val,sbon.kurs_val, " +
                    " sbon.dov_vkl where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' and sbon.vklady.KodVal=sbon.dov_val.CodeR030 and sbon.vklady.KodVal=sbon.kurs_val." +
                    "KodVal and sbon.vklady.ShifrVkl=sbon.dov_vkl.Shifr and sbon.vklady.vkltypaggr>1 group by GroupR031,KodStrok;";
            rs = statement.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
