package com.mahamart.stockindent
import android.app.*
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
class MainActivity:Activity(){
 data class Sku(val code:String,val name:String)
 data class Line(val code:String,val name:String,var qty:Int)
 val prefs by lazy{getSharedPreferences("indent",0)}
 val master= mutableListOf<Sku>();val lines= mutableListOf<Line>()
 var store="";lateinit var query:EditText;lateinit var results:LinearLayout;lateinit var cart:LinearLayout;lateinit var count:TextView;var pending=""
 override fun onCreate(b:Bundle?){super.onCreate(b);store=prefs.getString("store","")?:"";load();ui();if(store.isBlank())storeDialog()}
 fun ui(){val root=LinearLayout(this).apply{orientation=1;setPadding(16,16,16,12);setBackgroundColor(Color.rgb(246,248,251))}
 val sc=ScrollView(this);val body=LinearLayout(this).apply{orientation=1}
 body.addView(TextView(this).apply{text="MAHAMART\nStock Indent\nStore: "+if(store.isBlank())"Not configured" else store;textSize=25f;setTextColor(-1);setPadding(20,24,20,24);setBackgroundColor(Color.rgb(19,54,94))})
 val row=LinearLayout(this);row.addView(btn("Set store"){storeDialog()},LinearLayout.LayoutParams(0,-2,1f));row.addView(btn("Import SKU CSV"){val i=Intent(Intent.ACTION_OPEN_DOCUMENT).apply{addCategory(Intent.CATEGORY_OPENABLE);type="text/*"};startActivityForResult(i,10)},LinearLayout.LayoutParams(0,-2,1f));body.addView(row)
 body.addView(TextView(this).apply{text="FIND A PRODUCT";setPadding(2,14,0,6);setTextColor(Color.DKGRAY)})
 query=EditText(this).apply{hint="Search SKU code or name";singleLine=true};body.addView(query);results=LinearLayout(this).apply{orientation=1};body.addView(results)
 body.addView(TextView(this).apply{text="YOUR INDENT";setPadding(2,16,0,4);setTextColor(Color.DKGRAY)});count=TextView(this);body.addView(count);cart=LinearLayout(this).apply{orientation=1};body.addView(cart)
 val actions=LinearLayout(this);actions.addView(btn("Save draft"){save();toast("Draft saved")},LinearLayout.LayoutParams(0,-2,1f));actions.addView(btn("Export CSV"){export()},LinearLayout.LayoutParams(0,-2,1f));body.addView(actions)
 body.addView(btn("Last exported indent"){AlertDialog.Builder(this).setTitle("Export history").setMessage(prefs.getString("history","No export saved yet")?:"No export saved yet").setPositiveButton("OK",null).show()})
 query.addTextChangedListener(object:android.text.TextWatcher{override fun beforeTextChanged(s:CharSequence?,a:Int,c:Int,d:Int){};override fun onTextChanged(s:CharSequence?,a:Int,b:Int,c:Int){search(s.toString())};override fun afterTextChanged(s:android.text.Editable?){}})
 sc.addView(body);root.addView(sc,LinearLayout.LayoutParams(-1,0,1f));setContentView(root);search("");drawCart()}
 fun btn(t:String,f:()->Unit)=Button(this).apply{text=t;isAllCaps=false;textSize=12f;setOnClickListener{f()}}
 fun search(q:String){if(!::results.isInitialized)return;results.removeAllViews();if(q.isBlank()){results.addView(TextView(this).apply{text=if(master.isEmpty())"Import your SKU master CSV to begin." else "Search "+master.size+" products.";setTextColor(Color.GRAY)});return};master.filter{it.code.contains(q,true)||it.name.contains(q,true)}.take(25).forEach{s->results.addView(btn(s.name+"\nSKU: "+s.code){qty(s)})}}
 fun qty(s:Sku){val old=lines.find{it.code==s.code};val e=EditText(this).apply{hint="Quantity";inputType=2;setText(old?.qty?.toString()?:"")};AlertDialog.Builder(this).setTitle(s.name).setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save"){_,_->val n=e.text.toString().toIntOrNull();if(n==null||n<=0){toast("Quantity must be greater than zero");return@setPositiveButton};if(old==null)lines.add(Line(s.code,s.name,n))else old.qty=n;save();drawCart();query.setText("")}.show()}
 fun drawCart(){if(!::cart.isInitialized)return;cart.removeAllViews();count.text=lines.size.toString()+" unique SKUs";if(lines.isEmpty())cart.addView(TextView(this).apply{text="No products added yet.";setTextColor(Color.GRAY)});lines.toList().forEach{l->val r=LinearLayout(this);r.addView(TextView(this).apply{text=l.name+"\n"+l.code+" • Qty "+l.qty;setTextColor(Color.DKGRAY);setPadding(4,10,4,10)},LinearLayout.LayoutParams(0,-2,1f));r.addView(btn("Edit"){qty(Sku(l.code,l.name))});r.addView(btn("X"){lines.remove(l);save();drawCart()});cart.addView(r)}}
 fun storeDialog(){val e=EditText(this).apply{hint="Store name";setText(store)};AlertDialog.Builder(this).setTitle("Set store name").setMessage("This name is included in indent exports.").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save"){_,_->store=e.text.toString().trim();prefs.edit().putString("store",store).apply();ui()}.show()}
 @Deprecated("Legacy activity result API")
 override fun onActivityResult(req:Int,res:Int,d:Intent?){super.onActivityResult(req,res,d);if(res!=RESULT_OK||d?.data==null)return;try{if(req==10){val parsed=parse(d.data!!);if(parsed.isEmpty())throw Exception("No valid SKU rows; old master kept");master.clear();master.addAll(parsed);saveMaster();search(query.text.toString());toast("Imported "+master.size+" SKUs")}else if(req==11){contentResolver.openOutputStream(d.data!!)?.use{it.write(pending.toByteArray(Charsets.UTF_8))};prefs.edit().putString("history",pending).apply();toast("CSV saved")}}catch(e:Exception){toast("Import/export failed: "+e.message)}}
 fun parse(u:Uri):List<Sku>{val text=contentResolver.openInputStream(u)!!.bufferedReader(Charsets.UTF_8).use{it.readText()};val rows=text.lineSequence().filter{it.isNotBlank()}.map{it.splitCsv()}.toList();if(rows.isEmpty())return emptyList();val h=rows[0].map{it.trim().removePrefix("\uFEFF").lowercase(Locale.ROOT)};val ci=h.indexOfFirst{it=="sku code"||it=="sku"||it=="item code"||it=="product code"||it=="code"};val ni=h.indexOfFirst{it=="sku name"||it=="product name"||it=="item name"||it=="description"||it=="name"};if(ci<0||ni<0)throw Exception("Headers must include SKU code and SKU name");val m=linkedMapOf<String,Sku>();rows.drop(1).forEach{r->val c=r.getOrNull(ci)?.trim().orEmpty();val n=r.getOrNull(ni)?.trim().orEmpty();if(c.isNotEmpty()&&n.isNotEmpty())m[c]=Sku(c,n)};return m.values.toList()}
 fun String.splitCsv():List<String>{val a=mutableListOf<String>();val s=StringBuilder();var q=false;var i=0;while(i<length){val c=this[i];if(c=='"'&&q&&i+1<length&&this[i+1]=='"'){s.append('"');i++}else if(c=='"')q=!q else if(c==','&&!q){a.add(s.toString());s.setLength(0)}else s.append(c);i++};a.add(s.toString());return a}
 fun saveMaster(){val a=JSONArray();master.forEach{a.put(JSONObject().put("c",it.code).put("n",it.name))};prefs.edit().putString("master",a.toString()).apply()}
 fun load(){try{val a=JSONArray(prefs.getString("master","[]"));for(i in 0 until a.length()){val o=a.getJSONObject(i);master.add(Sku(o.getString("c"),o.getString("n")))};val d=JSONArray(prefs.getString("draft","[]"));for(i in 0 until d.length()){val o=d.getJSONObject(i);lines.add(Line(o.getString("c"),o.getString("n"),o.getInt("q")))}}catch(_:Exception){}}
 fun save(){val a=JSONArray();lines.forEach{a.put(JSONObject().put("c",it.code).put("n",it.name).put("q",it.qty))};prefs.edit().putString("draft",a.toString()).apply()}
 fun export(){if(store.isBlank()){storeDialog();return};if(lines.isEmpty()){toast("Add at least one SKU first");return};val date=SimpleDateFormat("yyyy-MM-dd",Locale.US).format(Date());val safe=store.replace(Regex("[^A-Za-z0-9_-]"),"_");pending=buildString{append("Store,SKU code,Sku name,QTY\r\n");lines.forEach{append(csv(store)).append(',').append(csv(it.code)).append(',').append(csv(it.name)).append(',').append(it.qty).append("\r\n")}};startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply{addCategory(Intent.CATEGORY_OPENABLE);type="text/csv";putExtra(Intent.EXTRA_TITLE,safe+"_Stock_Indent_"+date+".csv")},11)}
 fun csv(s:String)=if(s.any{it==','||it=='"'||it=='\n'||it=='\r'})"\""+s.replace("\"","\"\"")+"\"" else s
 fun toast(s:String)=Toast.makeText(this,s,Toast.LENGTH_LONG).show()
}