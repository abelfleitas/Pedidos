package abel.project.twa.vendedor.auxiliar;

import abel.project.twa.vendedor.modelos.BodegasResponse;
import abel.project.twa.vendedor.modelos.ClientesResponse;
import abel.project.twa.vendedor.modelos.ConvenioResponse;
import abel.project.twa.vendedor.modelos.FacturaResponse;
import abel.project.twa.vendedor.modelos.ObjFactor;
import abel.project.twa.vendedor.modelos.ObjPedido;
import abel.project.twa.vendedor.modelos.ObjPrecio;
import abel.project.twa.vendedor.modelos.ProductoResponse;
import abel.project.twa.vendedor.modelos.UserLoginResponse;
import abel.project.twa.vendedor.modelos.VendedorResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApiJson {


    @FormUrlEncoded
    @POST("api/login")
    Call<UserLoginResponse> login(
        @Field("CodVend") String codigo,
        @Field("Clave") String password
    );

    @GET("api/vendedores/{CodVend}")
    Call<VendedorResponse> getVendedorActual(
            @Path("CodVend") String codigo
    );

    @GET("api/clientes/text/{Param}")
    Call<ClientesResponse> getClienteByAnyWhere(
            @Path("Param") String codigo
    );

    @GET("api/bodegas")
    Call<BodegasResponse> getBodegas();

    @FormUrlEncoded
    @POST("api/bodegas/descripcion")
    Call<BodegasResponse> getBodega(
        @Field("descrip") String nombre
    );

    @GET("api/convenios/{CodConv}")
    Call<ConvenioResponse> getConvenio(
            @Path("CodConv") String convenio
    );

    @GET("api/productos/{CodProd}")
    Call<ProductoResponse> getProducto(
            @Path("CodProd") String producto
    );

    @GET("api/configuraciones/factor")
    Call<ObjFactor> getFactor();


    @GET("api/productos/{CodProd}/convenio/{CodConv}")
    Call<ProductoResponse> getProductoWhitConvenio(
            @Path("CodProd") String producto,
            @Path("CodConv") String convenio
    );

    @GET("api/productos/{CodProd}/convenio/{CodConv}/cantidad/{cantidad}")
    Call<ObjPrecio> getPrecioVarianteConvenio(
            @Path("CodProd") String producto,
            @Path("CodConv") String convenio,
            @Path("cantidad") String cantidad
    );

    @POST("api/facturas")
    Call<FacturaResponse> sendFactura(@Body ObjPedido objPedido);
}

