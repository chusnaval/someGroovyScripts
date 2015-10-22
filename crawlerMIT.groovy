def index = new URL("http://ocw.mit.edu/courses/electrical-engineering-and-computer-science/").getText("UTF-8")
def page = new StringBuilder()    
def universidad = new FileWriter("C:\\universidadMITUrbanismo.txt", true);
BufferedWriter fatalBuffer = new BufferedWriter(universidad)

def errores = new FileWriter("C:\\universidadMIT_Errores.txt", true);
BufferedWriter errorBuffer = new BufferedWriter(errores)

def class Crawler{

    def courses = ["electrical-engineering-and-computer-science","biological-engineering","health-sciences-and-technology","mathematics","economics",
        "mechanical-engineering","media-arts-and-sciences", "sloan-school-of-management","civil-and-environmental-engineering","linguistics-and-philosophy",
        "nuclear-engineering","engineering-systems-division","aeronautics-and-astronautics","sloan-school-of-management","biology"]
    def tablas = [:]
    def tablasErroneas = [:]
    
    def procesarUniversidad(page){
        courses.each{
            i-> buscarAsignaturas(page, i)
        }
    }   
     
    def buscarAsignaturas(page, course){
        def listaAsignaturas = buscarEnlaces(page, 'href="/courses/'+course+'/', '/')
        
        listaAsignaturas.each{
            i->procesarAsignatura(i, course)
        }    
    } 
     
    def buscarEnlaces(page, textoABuscar, finalEspecial){
        def startLink = 0
        def urlList = []
        while(startLink!=-1){
            startLink = page.indexOf(textoABuscar)

            if(startLink!=-1){
                def startQuote = page.toString().substring(startLink+textoABuscar.size(),page.size())
                def endLinkBarra = startQuote.indexOf(finalEspecial)
                def endLinkComillas = startQuote.indexOf('"')
                def minimo = endLinkBarra
                if(endLinkComillas<minimo)
                    minimo = endLinkComillas
                def url = startQuote.substring(0,minimo)
                urlList.add(url)
                page = startQuote.substring(0,startQuote.size())
            }
        }
        
        return urlList
    }   
    
    def procesarAsignatura(asignatura, course){
       procesarPagina("http://ocw.mit.edu/courses/"+course+"/"+asignatura+"/download-course-materials/\n")
    }
    
    def procesarPagina(pagina){
        try {
            def enlacesZip = buscarEnlaces(new URL(pagina).getText("UTF-8"), "<div class='downloadLink'><a href='", "'")
        
            enlacesZip.each{
                i-> tablas.put("ocw.mit.edu"+i,"a")
            }
        } catch (e) {
            tablasErroneas.put("${pagina}\n","a")
        }
        
    }
}

index.eachLine{
    line-> page.append(line);
}

crawler = new Crawler()
crawler.procesarUniversidad(page)

crawler.tablas.each{
    i-> universidad.write("${i.key}\n");
}
crawler.tablasErroneas.each{
    i-> errores.write("${i.key}\n");
}

fatalBuffer.flush();
fatalBuffer.close();
universidad.close();
errorBuffer.flush();
errorBuffer.close();
errores.close();
