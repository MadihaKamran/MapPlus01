package org.eclipse.jetty.embedded;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Handler extends AbstractHandler
{
   

    // public static String body = "lat383=43.68434&lng276=-79.56924&lng126=-79.47446&lng242=-79.55386&lng231=-79.55185&lng415=-79.60401&lat326=43.67213&lat479=43.68769&lat247=43.62566&lat188=43.61713&lat360=43.68116&lng260=-79.55961&lat384=43.68448&lat120=43.63375&lng296=-79.57737&lat174=43.62141&lng98=-79.46986&lat168=43.62282&lat119=43.6338&lat357=43.68034&lat47=43.64157&lng29=-79.47797&lat341=43.67431&lng485=-79.62135&lat402=43.6861&lat167=43.62306&lat36=43.64352&lat12=43.64997&lng381=-79.59729&lng160=-79.48917&lat298=43.67233&lng289=-79.57516&lat309=43.6722&lng44=-79.47489&lat325=43.67208&lng265=-79.56151&lng434=-79.60965&lat426=43.68543&lat424=43.68546&lat144=43.62835&lat334=43.67296&lng328=-79.5896&lat495=43.68627&lng488=-79.62128&lat93=43.63561&lng300=-79.57836&lat82=43.63723&lng152=-79.48218&lng46=-79.47494&lng277=-79.57012&lat157=43.62641&lat494=43.68639&lng436=-79.60981&lat323=43.67192&lng73=-79.47307&lat396=43.68581&lng218=-79.54989&lat316=43.67157&lng442=-79.61137&lat421=43.68545&lng339=-79.59135&lng8=-79.48397&lat239=43.61997&lat466=43.68806&lng175=-79.51157&lng19=-79.48038&lat306=43.67233&lng244=-79.55432&lng136=-79.47817&lng33=-79.47776&lng30=-79.47787&lng20=-79.48037&lng262=-79.56018&lng78=-79.47059&lng254=-79.55779&lng68=-79.4735&lat234=43.61873&lng326=-79.58932&lng138=-79.47843&lng466=-79.62047&lat457=43.68721&lat201=43.61486&lng132=-79.47664&lng297=-79.5777&lat187=43.61716&lat73=43.64028&lat436=43.68534&lng172=-79.50563&lat465=43.68803&lng64=-79.47413&lat287=43.67076&lat328=43.67226&lng210=-79.54828&lng62=-79.47425&lat491=43.68667&lat162=43.62438&lat442=43.68537&lat496=43.68622&lng449=-79.61302&lng371=-79.59528&lat24=43.64788&lat29=43.64408&lng344=-79.59196&lat414=43.68555&lng139=-79.47867&lat274=43.65614&lng204=-79.54703&lng228=-79.55108&lat74=43.64017&lng447=-79.61231&lng217=-79.54971&lat278=43.66115&lng352=-79.59263&lng426=-79.60689&lat392=43.68509&lat216=43.61496&lng88=-79.46901&lng327=-79.58941&lat286=43.67056&lat68=43.64055&lng206=-79.54739&lat470=43.68809&lng298=-79.57797&lng134=-79.477&lng15=-79.4804&lng192=-79.53717&lng460=-79.61863&lat317=43.6716&lng169=-79.50275&lat139=43.62971&lng104=-79.47047&lat138=43.6299&lng24=-79.4795&lng359=-79.59342&lat150=43.62769&lat254=43.63107&lat169=43.62261&lng256=-79.55871&lat249=43.62755&lat78=43.63791&lng117=-79.47234&lng166=-79.49982&lat260=43.63629&lat456=43.68704&lng252=-79.55696&lat90=43.63583&lat435=43.68534&lat28=43.6449&lat72=43.64035&lng370=-79.59495&lng486=-79.62129&lng459=-79.61847&lng325=-79.58921&lat114=43.63392&lng350=-79.59256&lng76=-79.47129&lng18=-79.48039&lng13=-79.48108&lng418=-79.60442&lat233=43.61857&lat16=43.65017&lat54=43.64142&lng194=-79.53755&lng28=-79.47828&lat499=43.68597&lng420=-79.605&lng189=-79.53074&lat381=43.68395&lat65=43.64083&lng107=-79.47079&lat347=43.67638&lng216=-79.54939&lng168=-79.50177&lng304=-79.57941&lat380=43.6838&lat186=43.61724&lat173=43.62178&lat221=43.61551&lat285=43.67031&lat48=43.64153&lat330=43.67246&lng354=-79.59281&lat374=43.68278&lat180=43.62005&lat219=43.61536&lat192=43.61582&lat116=43.63389&lat248=43.6267&lng127=-79.47483&lat427=43.68542&lat53=43.64143&lng287=-79.57479&lng380=-79.59703&lat91=43.63579&lng122=-79.47322&lng37=-79.47625&lng351=-79.59259&lng414=-79.6039&lng34=-79.47771&lng360=-79.59357&lat430=43.68537&lng188=-79.53045&lng251=-79.5568&lat205=43.61468&lng467=-79.62062&lat358=43.68074&lng292=-79.57596&lng40=-79.47514&lat342=43.67453&lng105=-79.47059&lat135=43.63061&lat111=43.63395&lng343=-79.59187&lng227=-79.55097&lat393=43.6852&lat472=43.68805&lat467=43.68809&lng468=-79.62076&lng0=-79.48482&lng6=-79.48445&lng261=-79.56013&lat264=43.63951&lng243=-79.55416&lng497=-79.6204&lat170=43.62238&lng455=-79.61634&lng489=-79.62125&lat349=43.67776&lat251=43.6285&lng149=-79.48163&lng310=-79.58282&lng182=-79.51743&lat104=43.63404&lng21=-79.48032&lat75=43.63986&lat474=43.68798&lng180=-79.51559&lat401=43.68607&lat333=43.67283&lng121=-79.47297&lat195=43.61582&lat454=43.68651&lng203=-79.5468&lat84=43.63713&lat378=43.68293&lat422=43.68545&lat26=43.6464&lng280=-79.57317&lat61=43.64103&lat460=43.68742&lat387=43.68473&lat27=43.64564&lat243=43.62253&lng4=-79.48464&lat4=43.64926&lng161=-79.49253&lat49=43.64151&lat319=43.67167&lng174=-79.50865&lat196=43.61565&lng443=-79.61144&lng341=-79.59156&lat80=43.63755&lng9=-79.48341&lng290=-79.57544&lat245=43.62323&lng112=-79.47168&lat296=43.67222&lat50=43.64149&lng225=-79.5507&lng364=-79.59393&lat46=43.64162&lat236=43.61924&lat161=43.62499&lng27=-79.47857&lng473=-79.62114&lat350=43.67878&lng282=-79.5735&lat88=43.6362&lng257=-79.55903&lat372=43.68267&lat321=43.67181&lat368=43.68212&lat109=43.63396&lng61=-79.47431&lng342=-79.59167&lng96=-79.46834&lat52=43.64146&lng391=-79.59885&lat67=43.64073&lat147=43.62798&lat40=43.6419&lng221=-79.55032&lng288=-79.57499&lat382=43.68396&lat60=43.64109&lat308=43.6722&lat431=43.68535&lng103=-79.47036&lat447=43.68547&lng52=-79.4749&lng427=-79.60708&lng361=-79.59368&lat455=43.68664&lng474=-79.62121&lng389=-79.59874&lng110=-79.47119&lng484=-79.62142&lng397=-79.5998&lat182=43.61968&lng205=-79.54719&lng475=-79.62127&lat212=43.61476&lat152=43.62755&lat123=43.63338&lat86=43.63674&lat112=43.63394&lat106=43.634&lng291=-79.57568&lng271=-79.56646&lng384=-79.5982&lat389=43.68491&lat449=43.6856&lat405=43.68609&lat7=43.64938&lat331=43.67259&lng295=-79.57702&lat395=43.68573&lat155=43.62728&lng60=-79.47437&lat371=43.68267&lng433=-79.60964&lat183=43.61864&lng115=-79.47208&lat43=43.64168&lng285=-79.57444&lng142=-79.48004&lat209=43.61467&lng69=-79.47336&lng357=-79.59306&lat115=43.6339&lat142=43.62865&lat108=43.63397&lng75=-79.47262&lng392=-79.59894&lng66=-79.47397&lng284=-79.57432&lat208=43.61466&lng386=-79.59845&lng338=-79.59134&lng196=-79.53894&lng246=-79.55493&lng306=-79.58022&lat425=43.68543&lng111=-79.47149&lat226=43.61607&lat291=43.67151&lat151=43.62766&lng235=-79.55246&lng309=-79.58125&lng439=-79.61049&lat222=43.61559&lng219=-79.55011&lng184=-79.5257&lat191=43.61582&lng170=-79.50382&lat102=43.63411&lat420=43.68545&lng330=-79.58994&lat33=43.64365&lat364=43.68159&lng31=-79.47782&lng355=-79.59284&lat105=43.63402&lng87=-79.46925&lng50=-79.47494&lat177=43.62022&lng472=-79.62106&lng303=-79.57909&lat8=43.64945&lng223=-79.55049&lng480=-79.62145&lat434=43.68534&lat464=43.68798&lat327=43.67217&lat340=43.67411&lat432=43.68534&lng407=-79.60122&lat132=43.63135&lng97=-79.46953&lng212=-79.54877&lng125=-79.47432&lng143=-79.48032&lng375=-79.59531&lat76=43.63856&lat451=43.68578&lng312=-79.5858&lng269=-79.56413&lat140=43.62941&lat211=43.61472&lng422=-79.60592&lat462=43.68759&lng454=-79.61587&lng279=-79.57135&lng70=-79.47329&lat312=43.67159&lat406=43.68608&lng441=-79.61118&lat149=43.62775&lat23=43.64865&lng163=-79.49776&lng461=-79.61887&lat97=43.63446&lng255=-79.55823&lng211=-79.54857&lng278=-79.57056&lat428=43.68541&lng222=-79.55039&lng372=-79.59528&lng181=-79.51616&lat143=43.62845&lat276=43.65806&lng322=-79.58877&lng67=-79.47386&lat267=43.64323&lng378=-79.59552&lng270=-79.56535&lng438=-79.61035&lng470=-79.62087&lng72=-79.47316&lng362=-79.59381&lat44=43.64168&lat438=43.68533&lat345=43.67559&lat410=43.68579&lng494=-79.62076&lat3=43.64922&lat448=43.68553&lat101=43.63414&lat497=43.68612&lng23=-79.47982&lat263=43.63837&lat204=43.61469&lng158=-79.48719&lng463=-79.61926&lat22=43.64942&lng106=-79.47069&lat304=43.67239&lng200=-79.54478&lat17=43.65013&lng135=-79.47751&lng74=-79.47294&lat141=43.62886&lng173=-79.50674&lng147=-79.48114&lng123=-79.47355&lat362=43.68145&lng83=-79.46995&lat25=43.64716&lat240=43.62067&lng493=-79.62091&lng198=-79.54202&lat261=43.63781&lat307=43.6723&lat131=43.63135&lat311=43.67162&lng377=-79.59536&lat463=43.68766&lat400=43.68605&lat485=43.68714&lat295=43.67212&lng154=-79.4827&lat1=43.64919&lng273=-79.56777&lng241=-79.5536&lat409=43.68581&lat197=43.61547&lat185=43.61802&lng366=-79.59416&lat203=43.6147&lng482=-79.62146&lng95=-79.46818&lat250=43.62812&lat171=43.62225&lng45=-79.47492&lat42=43.64177&lng428=-79.60717&lng77=-79.47101&lat337=43.67366&lat300=43.67238&lng90=-79.46872&lng365=-79.59403&lat369=43.68229&lat272=43.65385&lat343=43.67503&lat353=43.67952&lng41=-79.47511&lng395=-79.59959&lng183=-79.52275&lat99=43.63422&lat359=43.68096&lng268=-79.56335&lat206=43.61467&lng214=-79.54912&lng321=-79.58845&lat158=43.62629&lng22=-79.48012&lng294=-79.57666&lat199=43.6149&lat293=43.67183&lng128=-79.47517&lat133=43.63122&lng453=-79.6151&lat407=43.68606&lat335=43.67311&lng85=-79.46958&lat153=43.62747&lat488=43.68701&lat154=43.62739&lat486=43.68703&lat70=43.64043&lat318=43.67163&lat487=43.68702&lat439=43.68534&lat492=43.68657&lat175=43.62084&lat129=43.63223&lat229=43.61696&lng320=-79.58809&lng234=-79.55246&lat71=43.64039&lng238=-79.5529&lat228=43.61651&lng155=-79.48314&lat305=43.67237&lat294=43.67201&lat297=43.67229&lng79=-79.47044&lng159=-79.48822&lng464=-79.6202&lat458=43.68735&lat490=43.6868&lng450=-79.61339&lng369=-79.59473&lat273=43.65501&lat419=43.68546&lng3=-79.4847&lng413=-79.60361&lng456=-79.61749&lng263=-79.56033&lat283=43.66968&lng190=-79.53217&lng379=-79.59609&lat446=43.68544&lat146=43.62811&lng157=-79.48672&lat178=43.62018&lng86=-79.4695&lng199=-79.54478&lng408=-79.60219&lat469=43.6881&lng429=-79.60799&lat98=43.63427&lng318=-79.58759&lng412=-79.60335&lat227=43.61632&lng425=-79.60688&lng383=-79.59798&lng232=-79.55211&lat338=43.67391&lng162=-79.49508&lat461=43.68752&lat9=43.64959&lat258=43.63554&lat268=43.64586&lng207=-79.54766&lng12=-79.48171&lng499=-79.62014&lat329=43.67236&lng347=-79.59218&lat253=43.62969&lng140=-79.47906&lng388=-79.59865&lat271=43.65243&lat480=43.68764&lat475=43.68793&lat137=43.63004&lat18=43.6501&lng176=-79.5135&lng201=-79.54521&lng81=-79.47005&lat21=43.64989&lat218=43.61522&lat55=43.64141&lat282=43.66822&lng272=-79.56718&lat366=43.68181&lat257=43.63454&lng185=-79.52589&lng421=-79.60538&lat39=43.64196&lat58=43.64123&lng481=-79.62147&lng458=-79.61847&lat361=43.68129&lat6=43.64933&lat355=43.67982&lat416=43.68551&lng346=-79.59208&lat194=43.61585&lat471=43.68807&lng315=-79.5867&lat215=43.61491&lat41=43.64188&lat14=43.65026&lng267=-79.56222&lat235=43.61873&lng240=-79.55332&lat453=43.68623&lng401=-79.60028&lng226=-79.5508&lng7=-79.48427&lng167=-79.50068&lng491=-79.62104&lng239=-79.55301&lng16=-79.4804&lat320=43.67171&lat440=43.68535&lat100=43.63417&lat232=43.61816&lng220=-79.55023&lng137=-79.47826&lat69=43.64047&lat57=43.64143&lng38=-79.47536&lng305=-79.57981&lng250=-79.55664&lng141=-79.47975&lat190=43.61679&lat220=43.61544&lng349=-79.59239&lat417=43.6855&lng336=-79.59099&lat476=43.68788&lng36=-79.4776&lng144=-79.48048&lng469=-79.62078&lng186=-79.52989&lat244=43.62288&lng193=-79.53753&lng14=-79.4804&lat259=43.63619&lat159=43.62604&lng302=-79.5788&lat292=43.67168&lat127=43.63267&lng118=-79.47248&lng146=-79.48088&lat281=43.66764&lat66=43.64079&lat288=43.67098&lng236=-79.55269&lng233=-79.55232&lng259=-79.55958&lng11=-79.48182&lng130=-79.47574&lng131=-79.47663&lng101=-79.47016&lng93=-79.46854&lat386=43.68465&lat375=43.6828&lng416=-79.60411&lat336=43.67341&lng32=-79.47779&lat223=43.61569&lng5=-79.48456&lng253=-79.55727&lng317=-79.5873&lng179=-79.51537&lat81=43.63731&lat62=43.64098&lng445=-79.61177&lng435=-79.60978&lng476=-79.62133&lng479=-79.62144&lng496=-79.62054&lat403=43.68611&lng94=-79.46818&lat230=43.61741&lng215=-79.54927&lat415=43.68553&lng400=-79.60021&lng266=-79.56207&lng308=-79.58125&lat56=43.64142&lng274=-79.56834&lat391=43.68501&lat103=43.63407&lng209=-79.54814&lng2=-79.48474&lng358=-79.59328&lng430=-79.60799&lat310=43.67199&lat83=43.6372&lng116=-79.47222&lng345=-79.59204&lat30=43.64382&lng465=-79.62034&lng237=-79.55289&lng71=-79.47322&lat385=43.68455&lat266=43.64287&lat365=43.68169&lat377=43.68283&lat107=43.63398&lat241=43.62129&lng150=-79.4818&lat176=43.62045&lng440=-79.61082&lng286=-79.57462&lng387=-79.59855&lat210=43.61468&lng299=-79.57819&lat51=43.64147&lat128=43.63245&lng487=-79.62129&lat202=43.61478&lng197=-79.54026&lng108=-79.47089&lng48=-79.47496&lng229=-79.55138&lng307=-79.58051&lng164=-79.49886&lng26=-79.47887&lng368=-79.5945&lat399=43.686&lat94=43.6352&lng478=-79.62141&lat445=43.68541&lng80=-79.4703&lat200=43.6149&lat148=43.62782&lng92=-79.46863&lat20=43.65002&lat289=43.67114&lng114=-79.47186&lat217=43.61511&lat63=43.64093&lng394=-79.5995&lat356=43.67995&lat156=43.62652&lng410=-79.60274&lng402=-79.60043&lat122=43.63355&lng411=-79.60302&lng403=-79.60058&lat429=43.68537&lng49=-79.47495&lat315=43.67156&lat255=43.63224&lng165=-79.49952&lat270=43.65016&lat408=43.68589&lng448=-79.61267&lng59=-79.47442&lat207=43.61466&lng314=-79.58639&lat37=43.64264&lat225=43.61593&lat34=43.6436&lat324=43.67199&lng91=-79.46868&lng492=-79.62095&lng319=-79.58786&lng208=-79.54799&lat242=43.62189&lat184=43.61805&lat181=43.61993&lng334=-79.59058&lat284=43.67008&lng113=-79.47168&lng47=-79.47496&lat397=43.68589&lat31=43.64374&lat246=43.62422&lat473=43.68801&lat130=43.63203&lng406=-79.60109&lng247=-79.55557&lng264=-79.56074&lat468=43.6881&lng311=-79.58551&lat363=43.68154&lng348=-79.59225&lat231=43.61771&lat418=43.68548&lat322=43.67191&lat489=43.68696&lng374=-79.5953&lng202=-79.54589&lng337=-79.59118&lat238=43.61972&lng224=-79.55058&lat452=43.68591&lat394=43.68565&lat85=43.63682&lng316=-79.587&lat404=43.6861&lat482=43.68751&lng356=-79.59288&lng490=-79.62115&lat352=43.6791&lng390=-79.59885&lng471=-79.62097&lat19=43.65007&lat13=43.65012&lat172=43.622&lat423=43.68547&lng51=-79.47492&lng10=-79.48293&lng332=-79.59031&lat64=43.64088&lat279=43.6631&lng42=-79.47499&lat166=43.62325&lat134=43.63099&lat189=43.61707&lat346=43.67578&lng249=-79.55639&lng498=-79.62016&lat433=43.68534&lng363=-79.5939&lat351=43.67891&lng129=-79.47548&lat87=43.63648&lng452=-79.61411&lng424=-79.60662&lat198=43.61525&lng301=-79.57857&lng323=-79.58881&lat376=43.68282&lat95=43.6352&lat437=43.68533&lng248=-79.55602&lng409=-79.6026&lat275=43.65735&lng446=-79.61208&lat252=43.62888&lat256=43.63361&lat121=43.63368&lat388=43.68482&lng39=-79.47522&lat89=43.63588&lat390=43.68501&lat459=43.68735&lng133=-79.47668&lng145=-79.48073&lat412=43.68565&lat299=43.67236&lng156=-79.48625&lng57=-79.47465&lat163=43.62374&lng102=-79.47024&lng283=-79.57413&lat484=43.68732&lng124=-79.47401&lng457=-79.61801&lng333=-79.59045&lng58=-79.47448&lng423=-79.6064&lng100=-79.47009&lat15=43.65026&lng462=-79.61905&lat118=43.63384&lng385=-79.59831&lat165=43.62332&lng119=-79.47263&lat314=43.67156&lat125=43.63296&lng120=-79.47279&lat193=43.61585&lat124=43.63313&lng331=-79.59013&lng1=-79.48476&lat478=43.68776&lat481=43.68757&lat265=43.64151&lat348=43.67691&lat301=43.6724&lat92=43.63572&lat411=43.68572&lat332=43.67271&lng376=-79.59533&lat269=43.6476&lng404=-79.60074&lat379=43.68326&lng281=-79.57326&lng419=-79.60464&lat179=43.62009&lng178=-79.51494&lat126=43.63288&lng84=-79.46988&lat493=43.68653&lng25=-79.47921&lng477=-79.62137&lat96=43.63512&lat441=43.68536&lng65=-79.47405&lat280=43.66743&lng153=-79.48241&lat413=43.6856&lat237=43.61971&lat145=43.6282&lng230=-79.55167&lng82=-79.46998&lng245=-79.55448&lng148=-79.48147&lng444=-79.61163&lng63=-79.47419&lat344=43.6753&lat136=43.63011&lat443=43.68538&lng55=-79.47476&lat370=43.68245&lng17=-79.4804&lat32=43.64369&lng396=-79.5997&lat213=43.61481&lat214=43.61486&lat160=43.62581&lat224=43.6158&lng432=-79.60913&lng177=-79.51468&lng171=-79.50446&lng495=-79.62062&lat45=43.64164&lng54=-79.47481&lat164=43.62347&lat11=43.64994&lat303=43.6724&lng213=-79.54898&lng367=-79.59432&lng340=-79.59146&lat398=43.68596&lng187=-79.53031&lng353=-79.59274&lat38=43.64205&lng431=-79.6081&lat302=43.6724&lng324=-79.589&lng293=-79.57625&lng483=-79.62145&lat477=43.68782&lng35=-79.47765&lng99=-79.46998&lat290=43.67135&lat10=43.6497&lng313=-79.58602&lat313=43.67158&lat277=43.66007&lat339=43.67393&lat444=43.68539&lat113=43.63394&lng398=-79.59993&lat483=43.68743&lng195=-79.53776&lat354=43.67972&lng417=-79.60425&lng373=-79.59529&lng382=-79.59731&lng393=-79.59905&lat367=43.68196&lng89=-79.46876&lng437=-79.61008&lng151=-79.48187&lng451=-79.61369&lat59=43.64115&lng329=-79.58977&lat262=43.63797&lat77=43.63829&lng53=-79.47486&lat450=43.68569&lat373=43.68276&lat2=43.6492&lng335=-79.59074&lat35=43.64356&lng258=-79.55937&lng405=-79.6009&lat5=43.64929&lng191=-79.53717&lat117=43.63387&lng399=-79.60005&lat498=43.68597&lat110=43.63395&lng275=-79.56894&lng43=-79.47489&lng109=-79.471&lat79=43.63776&lat0=43.64914&lng56=-79.47472&";

     public static ArrayList<Point> getQueryMap(String query)  
     {  
         String[] params = query.split("&");  
         ArrayList<Point> route = new ArrayList<Point>(); 
         HashMap<Integer,Double> lat = new HashMap<Integer,Double>(); 
         HashMap<Integer,Double> lng = new HashMap<Integer,Double>(); 
         for (String param : params)  
         {  
             String name = param.split("=")[0];  
             double value = Double.parseDouble(param.split("=")[1]);
             int index = Integer.parseInt(name.substring(3));
            
             if(name.contains("lat"))
             {
               lat.put(index,value);
             }
             else if(name.contains("lng"))
             {
                lng.put(index,value);
             }
         }  
         for(int i = 0; i< lat.size(); i++)
         {
            route.add(new Point(lat.get(i),lng.get(i)));
         }
         return route;  
     }  


    public static String getBody(HttpServletRequest request) throws IOException 
    {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        String payloadRequest = getBody(request);
        ArrayList<Point> route = getQueryMap(payloadRequest);   

        System.out.println(route.size() + "var parsed" ); 
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();  

        if (payloadRequest != null)
        {  
           
            out.println("10 mins");
        }
        else
        {
            System.out.println("input coordinates are null");
        }

        baseRequest.setHandled(true);
    }
}