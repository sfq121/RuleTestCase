/**
 * @author xiaojingxian
 * @version 1.0
 * @date 2024/4/10 9:52 AM
 */
// {"comment":"DOCTYPE declarations are enabled for this DocumentBuilderFactory. This is vulnerable to XML external entity attacks. Disable this by setting the feature \"http://apache.org/xml/features/disallow-doctype-decl\" to true. Alternatively, allow DOCTYPE declarations and only prohibit external entities declarations. This can be done by setting the features \"http://xml.org/sax/features/external-general-entities\" and \"http://xml.org/sax/features/external-parameter-entities\" to false.","cwe":"CWE-611","end_line":125,"id":"5b838ea8806a48d5ab2813286fe5af11","start_line":125,"vulnerability":"Improper Restriction of XML External Entity Reference"}

package com.dic.boss.iop.util;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.dic.boss.iop.cache.DbConfigCache;
import com.dic.boss.iop.comm.CommKeys;
import com.dic.boss.iop.comm.Running;
import com.dic.boss.iop.dto.DtoForSender;
import com.dic.boss.iop.po.InfoOrder;
import com.dic.boss.iop.po.SnMappingPo;

/**
 * @author chailz
 * modified by Changping Chen
 *
 */
public class Tools {

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyyMMdd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(CommKeys.DATE_FORMAT3);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 从数据库获取现在时间
     *
     * @return 返回字符串格式yyyyMMddHHmmss
     */
    public static String getStringDate() {
        String sql = "vds.iop.qryDate1()";
        String dateString = Running.jdbcTemplate.queryForObject(sql, String.class);
        return dateString;
    }

    public static String dateFormat(long sqlDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(sqlDate);
        String strDate = format.format(date).toString();
        return strDate;
    }

    /**
     * 获取order_id
     *
     * @return order_id
     */
    public static String getOrderId() {
        String sql = "vds.iop.qryOrderIdSeq()";
        Map<String, Object> mapSequence = Running.jdbcTemplate.queryForMap(sql);
        String eopSequence = mapSequence.get(CommKeys.COL_EOP_ORDER_ID).toString();
        String current_time = getStringDate();
        String order_id = current_time + eopSequence;
        return order_id;
    }

    /**
     * 获取trans_id
     *
     * @return trans_id
     */
    public static String getTransId() {
        String sql = "vds.iop.qryTransIdSeq()";
        Map<String, Object> mapSequence = Running.jdbcTemplate.queryForMap(sql);
        System.out.println(mapSequence);
        String eopSequence = mapSequence.get(CommKeys.COL_EOP_SEQ_SN).toString();
        String appKey = DbConfigCache.paras.get(CommKeys.COL_EOP).get(CommKeys.COL_APPKEY);
        String current_time = getStringDateShort();
        String trans_id = appKey + current_time + eopSequence;
        return trans_id;
    }

    public static void errStack2Log4j(Exception e) {
        StackTraceElement[] stackTraces = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for (int i = 0; i < stackTraces.length; i++) {
            sb.append(stackTraces[i].toString()).append("\n");
        }
        Logger.getLogger("").error(sb.toString());
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        } else if (str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * String 转 XML org.w3c.dom.Document
     */
    public static Document stringToDoc(String xmlStr) throws Exception {
        // 字符串转XML
        Document doc = null;
        xmlStr = new String(xmlStr.getBytes(), CommKeys.CHAR_ENCODING);
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        doc = builder.parse(is);
        return doc;
    }

    // 启用事务
    @Transactional
    public static void saveBeforeSend(DtoForSender dtoForSender, SnMappingPo snMapping) {
        String sql = "vds.iop.addSnMapping(?)";
        for (int i = 0; i < dtoForSender.orderIdList.size(); i++) {
            Running.txJdbcTemplate.update(sql, snMapping.comm_sn, snMapping.service_sn, dtoForSender.sn, dtoForSender.orderIdList.get(i), "0", snMapping.user_id);
        }
        sql = "vds.iop.addIopMsg(?)";
        Running.txJdbcTemplate.update(sql, dtoForSender.sn, dtoForSender.sys_code, dtoForSender.reqMsg, dtoForSender.reqTime, dtoForSender.apiName,"0");
    }

    // 启用事务
    @Transactional
    public static void updateIopMsg(DtoForSender dtoForSender) {
        String sql = "vds.iop.updateREQForIopMsg(?)";
        Running.txJdbcTemplate.update(sql, dtoForSender.reqMsg, dtoForSender.sn);
    }

    // 启用事务
    @Transactional
    public static void saveAfterSend(String retStr, DtoForSender dtoForSender) {
        String sql = "vds.iop.updateIopMsg(?)";
        Running.txJdbcTemplate.update(sql, retStr, dtoForSender.sn);
    }

    public static String getDateStr(String format){

        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(date);

    }

    /**
     * 获取log_id
     * 1002+10位时间戳+4位序列
     * @return log_id
     */
    private static String getLogId(){
        String sql = "vds.iop.qryLogIdSeq()";
        String seqId = Running.txJdbcTemplate.queryForObject(sql, String.class);
        if(seqId.length()==1){
            seqId = "000" + seqId;
        }else if(seqId.length() == 2){
            seqId = "00" + seqId;
        }else if(seqId.length() == 3){
            seqId = "0" + seqId;
        }
        Date date = new Date();
        String time = date.getTime() + "";
        String log_id = "1002" + time.substring(3, 13) + seqId;
        return log_id;
    }

    /**
     * 修改info_order的order_status
     * @param service_sn
     * @param order_status
     */
    public static void modifyInfoOrder(String service_sn, String order_status){
        String infoOrderSql = "vds.iop.qryInfoOrder(?)";
        List<Map<String, Object>> infoOrderList = Running.txJdbcTemplate.queryForList(infoOrderSql, service_sn);
        infoOrderSql = "vds.iop.qryOrderId(?)";
        List<Map<String, Object>> subServiceSnList = Running.txJdbcTemplate.queryForList(infoOrderSql, service_sn);
        if(infoOrderList == null && subServiceSnList == null){
            Logger.getLogger("").info("===未查询到该【"+ service_sn +"】流水的订单信息===");
        }else{
            String orderId = "";
            if(subServiceSnList != null && subServiceSnList.size() > 0){
                Map<String, Object> mainServiceSnMap = subServiceSnList.get(0);
                orderId = mainServiceSnMap.get("MAIN_SERVICE_SN") + "";
            }else{
                orderId = service_sn;
            }
            infoOrderSql = "vds.iop.updateInfoOrder(?)";
            Running.txJdbcTemplate.update(infoOrderSql, order_status, orderId);
            final InfoOrder info_order = new InfoOrder();
            String log_id = getLogId();
            infoOrderSql = "vds.iop.qryInfoOrder(?)";
            Running.txJdbcTemplate.query(infoOrderSql, new Object[] { orderId }, new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    if(rs.getString("parent_order_id") != null){
                        info_order.parent_order_id = rs.getString("parent_order_id");
                    }
                    if(rs.getString("cust_id") != null){
                        info_order.cust_id=rs.getString("cust_id");
                    }
                    if(rs.getString("express_flag") != null){
                        info_order.express_flag=rs.getString("express_flag");
                    }
                    if(rs.getTimestamp("finish_date") != null){
                        info_order.finish_date=Tools.dateFormat(rs.getTimestamp("finish_date").getTime());
                    }
                    if(rs.getString("dev_channel_id") != null){
                        info_order.dev_channel_id=rs.getString("dev_channel_id");
                    }
                    if(rs.getString("dev_id") != null){
                        info_order.dev_id=rs.getString("dev_id");
                    }
                    if(rs.getString("create_operator_id") != null){
                        info_order.create_operator_id=rs.getString("create_operator_id");
                    }
                    if(rs.getTimestamp("create_date") != null){
                        info_order.create_date=Tools.dateFormat(rs.getTimestamp("create_date").getTime());
                    }
                    if(rs.getString("device_number") != null){
                        info_order.device_number=rs.getString("device_number");
                    }
                    if(rs.getString("jd_order_id") != null){
                        info_order.jd_order_id=rs.getString("jd_order_id");
                    }
                    if(rs.getString("user_event_code") != null){
                        info_order.user_event_code=rs.getString("user_event_code");
                    }
                    if(rs.getString("serv_code") != null){
                        info_order.serv_code=rs.getString("serv_code");
                    }
                    if(rs.getString("order_type") != null){
                        info_order.order_type=rs.getString("order_type");
                    }
                    if(rs.getString("storage_id") != null){
                        info_order.storage_id=rs.getString("storage_id");
                    }
                    if(rs.getString("tele_type") != null){
                        info_order.tele_type=rs.getString("tele_type");
                    }
                    if(rs.getString("contact_channel_id") != null){
                        info_order.contact_channel_id=rs.getString("contact_channel_id");
                    }
                }
            });
            infoOrderSql = "vds.iop.addLogInfoOrder(?)";
            Running.txJdbcTemplate.update(infoOrderSql,log_id,orderId,info_order.parent_order_id,info_order.cust_id,
                    info_order.express_flag,order_status,info_order.finish_date,info_order.dev_channel_id,info_order.dev_id,
                    info_order.create_operator_id,info_order.create_date,info_order.device_number,info_order.jd_order_id,
                    info_order.user_event_code,info_order.serv_code,info_order.order_type,info_order.storage_id,
                    info_order.tele_type,info_order.contact_channel_id);
        }
    }

    public static void checkOrderStatus(String api_name, String order_id, String order_status){
        if(!"orders.deleteProd2".equals(api_name)){
            modifyInfoOrder(order_id, order_status);
        }else{
            String sql = "vds.iop.qryInfoOrder(?)";
            List<Map<String, Object>> infoOrderList = Running.txJdbcTemplate.queryForList(sql, order_id);
            if(infoOrderList == null){
                Logger.getLogger("").info("===未查询到该【"+ order_id +"】流水的订单信息===");
            }else{
                String orderStatus = infoOrderList.get(0).get("ORDER_STATUS") + "";
                if(!"F".equals(orderStatus) && "T".equals(orderStatus)){
                    modifyInfoOrder(order_id, order_status);
                }else{
                    Logger.getLogger("").info("===该【"+ order_id +"】流水的订单状态【" + orderStatus + "】不在修改范围之内===");
                }
            }
        }
    }
}

