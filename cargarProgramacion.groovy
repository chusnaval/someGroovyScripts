def index = 'http://www.laguiatv.com/programacion'.toURL().text

index.eachLine{
    line-> buscarEnlaces(line);
}

def buscarEnlaces(str)
{
    def enlace = '<a href="/programacion/'
    if(str.contains(enlace) && str.contains("title")){
        procesarLinea(str)
    }
}

def procesarLinea(str){
    def enlace = '<a href="/programacion/'

    if(str.indexOf("title")>str.indexOf(enlace)+enlace.length())
    {
         leerPagina("http://www.laguiatv.com/"+str.substring(str.indexOf(enlace)+10,str.indexOf("title")-2))
    }
   
}

def leerPagina(page)
{
    def pagina = page.toURL().text
    def StringReader s=new StringReader(pagina)
    def xmldoc=groovy.xml.DOMBuilder.parse(s)
    def names=xmldoc.documentElement

    println names 

    use (groovy.xml.dom.DOMCategory){
        println emisiones.text()

    }
}

def encontrarProgramacion(str){
    
}