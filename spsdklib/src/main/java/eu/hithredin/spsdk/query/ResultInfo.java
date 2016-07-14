package eu.hithredin.spsdk.query;


/**
 * Informations about the query result
 */
public class ResultInfo {

    public ResultInfo(BaseRequestBuilder request) {
        tag = request.tag;
        idQuery = request.getQueryId();
    }

    public static ResultInfo getSuccess() {
        ResultInfo ri = new ResultInfo();
        ri.statusResult = ORDER_RESULT.first;
        ri.codeQuery = CODE_QUERY.SUCCESS;
        return ri;
    }

    public static ResultInfo getError() {
        ResultInfo ri = new ResultInfo();
        ri.statusResult = ORDER_RESULT.first;
        ri.codeQuery = CODE_QUERY.SERVER_ERROR;
        return ri;
    }

    /**
     * Order of this result. Sometime the callback is called twice if cache (first) is sent before data query (last)
     */
    public enum ORDER_RESULT {
        only, first, last
    }


    public enum CODE_QUERY {
        SUCCESS, SERVER_ERROR, TIMEOUT_ERROR, NETWORK_ERROR, NOT_AUTHORIZED, NOT_FOUND, ERROR_ALREADY_MANAGED
    }

    public int idQuery;
    public int httpCode;


    /**
     * Status query result.
     */
    public CODE_QUERY codeQuery;


    public ORDER_RESULT statusResult;

    /**
     * Timestamp Unix (POSIX) in millisecond of the receiving of query result.
     */
    public long spentTime = 0;

    public Object tag;

    public ResultInfo() {
    }

    public ResultInfo(CODE_QUERY codeQuery) {
        this.codeQuery = codeQuery;
    }

    /**
     * @return True if the query is a success. False otherwise.
     */
    public boolean isSuccess() {
        return codeQuery == CODE_QUERY.SUCCESS;
    }

}
