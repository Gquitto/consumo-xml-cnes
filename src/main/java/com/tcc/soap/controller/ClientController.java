package com.tcc.soap.controller;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;

import jdk.internal.org.xml.sax.SAXException;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@RestController
@RequestMapping(value = "/api/CnesClient/")
public class ClientController {

    @CrossOrigin
    @GetMapping("getCnes/{id}")
    public String getAllPosts(@PathVariable("id") String id) throws IOException, SAXException, ParserConfigurationException {

        String XMLSRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:est=\"http://servicos.saude.gov.br/cnes/v1r0/estabelecimentosaudeservice\" xmlns:fil=\"http://servicos.saude.gov.br/wsdl/mensageria/v1r0/filtropesquisaestabelecimentosaude\" xmlns:cod=\"http://servicos.saude.gov.br/schema/cnes/v1r0/codigocnes\" xmlns:cnpj=\"http://servicos.saude.gov.br/schema/corporativo/pessoajuridica/v1r0/cnpj\">\n" +
                "<soap:Header>\n" +
                "      <wsse:Security soap:mustUnderstand=\"true\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "         <wsse:UsernameToken wsu:Id=\"UsernameToken-5FCA58BED9F27C406E14576381084652\">\n" +
                "            <wsse:Username>CNES.PUBLICO</wsse:Username>\n" +
                "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">cnes#2015public</wsse:Password>\n" +
                "         </wsse:UsernameToken>\n" +
                "      </wsse:Security>\n" +
                "</soap:Header>\n" +
                "\n" +
                "   <soap:Body>\n" +
                "      <est:requestConsultarEstabelecimentoSaude>\n" +
                "         <fil:FiltroPesquisaEstabelecimentoSaude>\n" +
                "            <cod:CodigoCNES>\n" +
                "               <cod:codigo>"+ id +"</cod:codigo>\n" +
                "            </cod:CodigoCNES>\n" +
                "         </fil:FiltroPesquisaEstabelecimentoSaude>\n" +
                "      </est:requestConsultarEstabelecimentoSaude>\n" +
                "   </soap:Body>\n" +
                "</soap:Envelope>";

        String url = "https://servicos.saude.gov.br/cnes/EstabelecimentoSaudeService/v1r0";

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

// add request header
        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

// Send post request
        con.setDoOutput(true);
        OutputStream os = null; //get output Stream from con

        try {
            os = con.getOutputStream();
            os.write( XMLSRequest.getBytes("utf-8") );
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.print(con.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine = "";
        StringBuffer response = new StringBuffer();

        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.append(inputLine + '\n');
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response.toString();
    }
}
