package org.ctfcracktools.fuction

import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Base64.*
import org.apache.commons.text.StringEscapeUtils
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.swing.JOptionPane

/**
 * @author 林晨0chencc
 * @since 2017/12/2
 * @version 1.0.2
 */
class CoreFunc{
    /**
     * 主函数
     * @param input 需要加密/解密的字符串传入
     * @param type 加密类型
     * @return String 编码后的结果
     */
    fun callFunc(input:String,type:String): String? {
        val funcMap = mutableMapOf(CodeMode.CRYPTO_FENCE to ::fence,
            CodeMode.CRYPTO_CAESAR to ::caesar,
            CodeMode.CRYPTO_PIG to ::pigCode,
            CodeMode.CRYPTO_ROT13 to ::rot13,
            CodeMode.CRYPTO_HEX_2_STRING to ::hextoString,
            CodeMode.CRYPTO_STRING_2_HEX to ::stringtoHex,
            CodeMode.CRYPTO_UNICODE_2_ASCII to ::unicodeToAscii,
            CodeMode.CRYPTO_ASCII_2_UNICODE to ::asciiToUnicode,
            CodeMode.CRYPTO_REVERSE to ::reverse,

            CodeMode.DECODE_MORSE to ::morseDecode,
            CodeMode.DECODE_BACON to ::baconCodeDecode,
            CodeMode.DECODE_BASE64 to ::base64de,
            CodeMode.DECODE_BASE32 to ::base32de,
            CodeMode.DECODE_URL to ::urlDecoder,
            CodeMode.DECODE_UNICODE to ::unicodeDecode,
            CodeMode.DECODE_HTML to ::htmlDecode,
            CodeMode.DECODE_VIGENERE to ::vigenereDeCode,

            CodeMode.ENCODE_MORSE to ::morseEncode,
            CodeMode.ENCODE_BACON to ::baconCodeEncode,
            CodeMode.ENCODE_BASE64 to ::base64en,
            CodeMode.ENCODE_BASE32 to ::base32en,
            CodeMode.ENCODE_URL to ::urlEncoder,
            CodeMode.ENCODE_UNICODE to ::unicodeEncode,
            CodeMode.ENCODE_HTML to ::htmlEncode,
            CodeMode.ENCODE_VIGENERE to ::vigenereEnCode,)

        return funcMap[type]?.let { it(input) }
    }

    /**
     * 栅栏密码
     * @param input 待加密解密的字符串
     * @return String
     */
    private fun fence(input: String):String {
        val str:Array<String?> = arrayOfNulls<String>(1024)
        val x = IntArray(1024)
        val result = StringBuffer()
        var a = 0
        var nums = 0
        if (input.length!=1&&input.length!=2) {
            (2 until input.length).forEach { i ->
                if (input.length % i == 0) {
                    x[a] = i
                    a++
                }
            }
        }
        if(a!=0) {
            (0 until a).forEach { i ->
                result.append("${i + 1}：")
                (0 until input.length / x[i]).forEach { j ->
                    str[nums] = input.substring(0 + (x[i] * j), x[i] + (x[i] * j))
                    nums++
                }
                (0 until str[0]!!.length).forEach { ji ->
                    (0 until nums).forEach { s -> result.append(str[s]!!.substring(ji,ji+1)) }
                }
                nums =0
                result.append("\n")
            }
        }else{
            val newstrlenth:Int = input.replace(" ","").length
            if (newstrlenth !=1 && newstrlenth !=2){
                (2..newstrlenth-1).forEach { i ->
                    if (newstrlenth%i==0){
                        x[a]=i
                        a++
                    }
                }
            }
            if (a != 0) {
                (0 until a).forEach { it -> result.append(" " + x[it]) }
                (0 until a).forEach { i ->
                    result.append("${i+1}：")
                    (0 until newstrlenth / x[i]).forEach { j ->
                        str[nums] = input.substring(0 + x[i] * j, x[i] + x[i] * j)
                        nums++
                    }
                    (0 until str[0]!!.length).forEach { j ->
                        (0 until nums).forEach { result.append(str[it]!!.substring(j, j + 1)) }
                    }
                    nums = 0
                    result.append('\n')
                }
            }
        }
        return result.toString()
    }

    /**
     * 凯撒密码/Caesar Code
     * @param input 待加密解密的字符串
     * @return String
     */
    private fun caesar(input:String):String{
        val word:CharArray=input.toCharArray()
        val result = StringBuffer()
        (0 until 26).forEach {
            (word.indices).forEach { j ->
                if (word[j].isUpperCase()){
                    if(word[j]=='Z') word[j]='A' else word[j] = (word[j].toInt() + 1).toChar()
                }else if(word[j].isLowerCase()){
                    if(word[j]=='z') word[j]='a' else word[j]=(word[j].toInt()+1).toChar()
                }
            }
            result.append(word+'\n')
        }
        return result.toString()
    }

    /**
     * 维吉利亚密码
     * @param input 待加解密的字符串
     * @param mode VIGENERE_MODE_DECODE为解密 VIGENERE_MODE_DECODE为加密
     * @return String
     */
    private fun vigenereCode(input:String,mode:String):String{
        val pass = input.toCharArray()
        val key = JOptionPane.showInputDialog("Please input key").toCharArray()
        var i = 0
        var j:Int
        var q = 0
        var k:Int
        var m:Int
        when(mode){
            CodeMode.DECODE_VIGENERE -> return StringBuilder()
                .let{
                        result ->
                    while (i<pass.size) {
                        when {
                            pass[i].isUpperCase() -> {
                                j = q%key.size
                                k = UpperCase.indexOf(key[j].toUpperCase())
                                m = UpperCase.indexOf(pass[i])
                                result.append(UpperCase[(m+k)%26])
                                q++
                            }
                            pass[i].isLowerCase() -> {
                                j = q%key.size
                                k = LowerCase.indexOf(key[j].toLowerCase())
                                m = LowerCase.indexOf(pass[i])
                                result.append(LowerCase[(m+k)%26])
                                q++
                            }
                            else -> result.append(pass[i])
                        }
                        i++
                    }
                    result
                }
                .toString()
            CodeMode.ENCODE_VIGENERE -> return StringBuilder()
                .let{
                        result ->
                    while (i<pass.size) {
                        when {
                            pass[i].isUpperCase() -> {
                                j = q%key.size
                                k = UpperCase.indexOf(key[j].toUpperCase())
                                m = UpperCase.indexOf(pass[i])
                                if(m<k)
                                    m+=26
                                result.append(UpperCase[m-k])
                                q++
                            }
                            pass[i].isLowerCase() -> {
                                j = q%key.size
                                k = LowerCase.indexOf(key[j].toLowerCase())
                                m = LowerCase.indexOf(pass[i])
                                if(m<k)
                                    m+=26
                                result.append(LowerCase[m-k])
                                q++
                            }
                            else -> result.append(pass[i])
                        }
                        i++
                    }
                    result
                }
                .toString()
            else -> return "None"
        }
    }

    /**
     * 维吉利亚密码加密 依赖vigenereCode
     * @param input 待加密的字符串
     * @return String
     */

    private fun vigenereEnCode(input:String): String = vigenereCode(input, CodeMode.ENCODE_VIGENERE)

    /**
     * 维吉利亚解密 依赖vigenereCode
     * @param input 待解密的字符串
     * @return String
     */
    private fun vigenereDeCode(input:String):String = vigenereCode(input, CodeMode.DECODE_VIGENERE)

    /**
     * 猪圈密码
     * @param input 待解密的字符串
     * @return String
     */
    private fun pigCode(input:String):String{
        val result = StringBuffer()
        val keymap= mapOf('A' to 'J','B' to 'K','C' to 'L','D' to 'M',
            'E' to 'N','F' to 'O','G' to 'P','H' to 'Q','I' to 'R','J' to 'A','K' to 'B','L' to 'C',
            'M' to 'D','N' to 'E','O' to 'F','P' to 'G','Q' to 'H','R' to 'I','S' to 'W','T' to 'X',
            'U' to 'Y','V' to 'Z','W' to 'S','X' to 'T')
        val word = input.toCharArray()
        for (i in word.indices){
            if (word[i].isUpperCase()){
                result.append(keymap.get(word[i])!!)
            }else if(word[i].isLowerCase()){
                result.append(keymap.get(word[i].toUpperCase())!!.toLowerCase())
            }else{
                result.append(word[i])
            }
        }
        return result.toString()
    }

    /**
     * ROT13
     * @param input 待编码的字符串
     * @return String
     */
    private fun rot13(input:String):String{
        var word = input.toCharArray()
        val result = StringBuffer()
        for (i in 0 until word.size){
            when {
                word[i] in 'a'..'m' -> word[i]=(word[i].toInt()+13).toChar()
                word[i] in 'A'..'M' -> word[i]=(word[i].toInt()+13).toChar()
                word[i] in 'n'..'z' -> word[i]=(word[i].toInt()-13).toChar()
                word[i] in 'N'..'Z' -> word[i]=(word[i].toInt()-13).toChar()
            }
            result.append(word[i])
        }
        return result.toString()
    }

    /**
     * 培根密码加密
     * @param input 待加密的字符串
     * @return String
     */
    private fun baconCodeEncode(input:String):String{
        val keymap = mapOf("A" to "aaaaa","B" to "aaaab","C" to "aaaba",
            "D" to "aaabb","E" to "aabaa","F" to "aabab","G" to "aabba","H" to "aabbb","I" to "abaaa",
            "J" to "abaab","K" to "ababa","L" to "ababb","M" to "abbaa","N" to "abbab","O" to "abbba",
            "P" to "abbbb","Q" to "baaaa","R" to "baaab","S" to "baaba","T" to "baabb","U" to "babaa",
            "V" to "babab","W" to "babba","X" to "babbb","Y" to "bbaaa","Z" to "bbaab","a" to "AAAAA",
            "b" to "AAAAB","c" to "AAABA","d" to "AAABB","e" to "AABAA","f" to "AABAB","g" to "AABBA",
            "h" to "AABBB","i" to "ABAAA","j" to "ABAAB","k" to "ABABA","l" to "ABABB","m" to "ABBAA",
            "n" to "ABBAB","o" to "ABBBA","p" to  "ABBBB","q" to "BAAAA",
            "r" to "BAAAB","s" to "BAABA","t" to "BAABB","u" to "BABAA","v" to "BABAB","w" to "BABBA",
            "x" to "BABBB","y" to "BBAAA","z" to "BBAAB")
        return StringBuilder()
            .let{
                    result ->
                if(is26word(input)) {
                    splitNum(input,1).forEach {result.append(keymap[it])}
                }else{
                    result.append("有字符不属于26字母其中")
                }
                result
            }
            .toString()
    }

    /**
     * 培根密码解密
     * @param input 待加密的字符串
     * @return String
     */
    private fun baconCodeDecode(input: String):String{
        val keymap = mapOf("aaaaa" to 'A',"aaaab" to 'B',"aaaba" to 'C',
            "aaabb" to 'D',"aabaa" to 'E',"aabab" to 'F',"aabba" to 'G',"aabbb" to 'H',"abaaa" to 'I',
            "abaab" to 'J',"ababa" to 'K',"ababb" to 'L',"abbaa" to 'M',"abbab" to 'N',"abbba" to 'O',
            "abbbb" to 'P',"baaaa" to 'Q',"baaab" to 'R',"baaba" to 'S',"baabb" to 'T',"babaa" to 'U',
            "babab" to 'V',"babba" to 'W',"babbb" to 'X',"bbaaa" to 'Y',"bbaab" to 'Z',//UpperCase大写字符
            "AAAAA" to 'a',"AAAAB" to 'b',"AAABA" to 'c',"AAABB" to 'd',"AABAA" to 'e',"AABAB" to 'f',
            "AABBA" to 'g',"AABBB" to 'h',"ABAAA" to 'i',"ABAAB" to 'j',"ABABA" to 'k',
            "ABABB" to 'l',"ABBAA" to 'm',"ABBAB" to 'n',"ABBBA" to 'o', "ABBBB" to 'p',"BAAAA" to 'q',
            "BAAAB" to 'r',"BAABA" to 's',"BAABB" to 't',"BABAA" to 'u',"BABAB" to 'v',"BABBA" to 'w',
            "BABBB" to 'x',"BBAAA" to 'y',"BBAAB" to 'z'
        )
        return StringBuilder()
            .let{
                    result ->
                if(isBacon(input)){
                    val inputnew = input.replace(" ","")
                    splitNum(inputnew,5,"[ab]{5}").forEach { result.append(keymap[it]) }
                }else{
                    result.append("并非是培根密码")
                }
                result
            }
            .toString()
    }

    private fun base64de(input: String):String = String(decodeBase64(input))
    private fun base64en(input: String):String = encodeBase64String(input.toByteArray())
    private fun base32de(input:String):String = String(Base32().decode(input))
    private fun base32en(input:String):String = Base32().encodeAsString(input.toByteArray())

    /**
     * 16进制转字符串
     * @param input 待编码的字符串
     * @return String
     */
    private fun hextoString(input:String):String{
        return StringBuilder()
            .let{
                    result ->
                (0 until input.length-1 step 2)
                    .map{ input.substring(it, it+2) }
                    .map { Integer.parseInt(it,16) }
                    .forEach { result.append(it.toChar()) }
                result
            }
            .toString()
    }

    /**
     * 字符串转16进制
     * @param input 待编码的字符串
     * @return String
     */
    private fun stringtoHex(input:String):String{
        return StringBuilder()
            .let{
                    result ->
                input.toCharArray().forEach {result.append(Integer.toHexString(it.toInt())) }
                result
            }
            .toString()
    }

    /**
     * 摩斯电码编码
     * @param input 待编码的字符串
     * @return String
     */
    private fun morseEncode(input: String):String {
        return StringBuilder()
            .let{
                    result ->
                input.toLowerCase().toCharArray().forEach {
                    when {
                        isChar(it) -> result.append(morseCharacters[(it - 'a')]+" ")
                        isDigit(it) -> result.append(morseDigits[(it - '0')]+" ")
                    }
                }
                result
            }
            .toString()
    }

    /**
     * 摩斯密码解码
     * @param input 待解码的字符串
     * @return String
     */
    private fun morseDecode(input:String):String{
        initMorseTable()
        val morse=format(input)
        val st=StringTokenizer(morse)
        val result=StringBuilder(morse.length / 2)
        while (st.hasMoreTokens()) {
            result.append(htMorse[st.nextToken()])
        }
        return result.toString()
    }

    private fun urlEncoder(input: String):String = URLEncoder.encode(input,"utf-8").replace("+","%20")//Url编码加密
    private fun urlDecoder(input: String):String = URLDecoder.decode(input,"utf-8")//Url编码解密
    private fun unicodeEncode(input: String):String{
        return StringBuilder()
            .let {
                    result ->
                input.toCharArray().forEach {result.append("\\u"+Integer.toHexString(it.toInt())) }
                result
            }
            .toString()
    }
    private fun unicodeDecode(input: String):String{
        return StringBuilder()
            .let{
                    result ->
                val hex=input.split("\\u")
                (1 until hex.size)
                    .map { Integer.parseInt(hex[it], 16) }
                    .forEach { result.append(it.toChar()) }
                result
            }
            .toString()
    }

    /**
     * Unicode编码转Ascii编码
     * @param input 待编码的字符串
     * @return String
     */
    private fun unicodeToAscii(input:String):String{
        return StringBuilder()
            .let{
                    result ->
                val pout = Pattern.compile("\\&\\#(\\d+)").matcher(input)
                while (pout.find()){ result.append(Integer.valueOf(pout.group(1)).toInt().toChar()) }
                result
            }
            .toString()
    }

    /**
     * Ascii编码转Unicode编码
     * @param input 待编码的字符串
     * @return String
     */
    private fun asciiToUnicode(input: String):String{
        return StringBuilder()
            .let {
                    result ->
                input.toCharArray().forEach { result.append("&#"+it.toInt()+";")}
                result
            }
            .toString()
    }

    /**
     * 字符翻转，顾名思义
     * @param input 待翻转的字符串
     * @return String
     */
    private fun reverse(input:String):String{
        val result = StringBuilder()
        val tmp = input.toCharArray()
        var a=tmp.size-1
        while (a > -1) {
            result.append(tmp[a])
            a-=1
        }
        return result.toString()
    }
    /*    fun HillCodeEncode(input:String,key:String):String{
            val keymatrix:CharArray = key.split(" ") as CharArray
            var tmp[]:<String?>Array
            when{
                keymatrix.size/4 == 0 ->
                    0 until keymatrix.size%4.forEach{

            }

            }
            return StringBuffer()
                    .let {
                        result->

                    }
                    .toString()
        }*/
    private fun htmlEncode(input:String): String = StringEscapeUtils.escapeHtml4(input)
    private fun htmlDecode(input:String):String = StringEscapeUtils.unescapeHtml4(input)
    /* 内置方法/内置常量 */
    private val UpperCase:String ="ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val LowerCase:String = "abcdefghijklmnopqrstuvwxyz"
//    private val SplitString = {//多次切割字符串
//            input:String ->
//        val tmp = input.split(",")
//        val result = StringBuilder()
//        tmp.forEach{
//            val tmp_1 = it.split(" to ")
//            result.append(tmp_1[1]+" to "+tmp_1[0]+",")
//        }
//        result.toString()
//    }
    private fun splitNum(input:String,num:Int):Array<String?>{
        val str:Array<String?> = arrayOfNulls(input.length/num)
        (1..input.length/num).forEach { i->str.set(i-1,input.substring(i*num-num,i*num)) }
        return str
    }
    private fun splitNum(input:String,num:Int,pattern:String):Array<String?>{
        val r:Pattern = Pattern.compile(pattern)
        val m: Matcher = r.matcher(input)
        var input_m = ""
        var a = true
        while (a){
            if(!m.find()) a=false else input_m += m.group()
        }
        return splitNum(input_m,num)
    }
    val isBacon = {
            input:String ->
        var tmp:Boolean = false
        input.toCharArray().forEach {
            tmp = (it=='A'||it=='B')||(it == 'a'||it=='b')
        }
        tmp
    }
    val is26word = {
            input:String ->
        var tmp = false
        input.toCharArray().forEach {
            tmp = isChar(it)
        }
        tmp
    }
    var htMorse: Hashtable<String, Char> = Hashtable()
    private fun initMorseTable(){
        (0..25).forEach { i -> htMorse.put(morseCharacters[i], Character.valueOf((65+i).toChar())) }
        (0..9).forEach { i -> htMorse.put(morseDigits[i], Character.valueOf((48+i).toChar())) }
    }
    val isChar = {c:Char -> c.isLowerCase()||c.isUpperCase()}
    val isDigit = {c:Char -> (c >='0')&&(c <= '9')}
    /* moresCode */
    private val morseCharacters = arrayOf(".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "src", ".---",
        "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--",
        "--..")
    private val morseDigits = arrayOf("-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...",
        "---..", "----.")
    val format = {
            input:String->
        val word = input.toCharArray()
        val result = StringBuilder()
        for(i in word.indices){
            when{
                word[i] == '\n' -> word[i]=' '
                word[i] == '.' -> {
                    result.append(word[i])
                }
                word[i] == '-' -> {
                    result.append(word[i])
                }
                word[i] == ' ' -> {
                    result.append(word[i])
                }
            }
        }
        result.toString()
    }
}
