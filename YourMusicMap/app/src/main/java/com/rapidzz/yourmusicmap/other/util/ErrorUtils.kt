import com.rapidzz.mymusicmap.datamodel.model.responses.ApiErrorResponse
import org.json.JSONObject
import java.lang.Exception


object ErrorUtils {

    fun parseError(json: String): ApiErrorResponse {
       try {
           val json = JSONObject(json)
           val error = ApiErrorResponse(
               json.optInt("code", 0),
               json.optString("message", ""),
               json.optString("name", "")
           )
           return error
       }catch (ex: Exception){
           return ApiErrorResponse(0,"","")
       }
    }

    fun parseError(t: Throwable): ApiErrorResponse {
        try {
            return ApiErrorResponse(0, t.message!!, t.message!!)
        }catch (ex: Exception){
            ex.printStackTrace()
            return ApiErrorResponse(0,"","")
        }
    }

}