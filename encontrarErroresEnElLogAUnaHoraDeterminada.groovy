class Buscador{
    int margen = 10;
    int minutoInicio = 01;
    int minutoFin = 30;
    def fechasHoras = ["2012-03-16 14:", "2012-03-13 13:27:", "2012-03-15 12:46:", "2012-03-15 13:06:","2012-03-15 13:13:","2012-03-15 18:32:","2012-03-16 01:19:","2012-03-16 09:51:"];
    boolean append = true;
    def inicio = new FileWriter("D:\\logInicio.txt", append);
    BufferedWriter inicioBuffer = new BufferedWriter(inicio)
    def fin = new FileWriter("D:\\logFin.txt", append);
    BufferedWriter finBuffer = new BufferedWriter(fin)
    def errores = new FileWriter("D:\\logFatal.txt", append);
    BufferedWriter fatalBuffer = new BufferedWriter(errores)
            
    def buscarAntesYDespues(){
        def directorio = new File("D:\\logs")
        File[] logs = directorio.listFiles()
        fechasHoras.each { 
            i -> buscarPorFecha(logs,i);
        }
         
        finBuffer.flush();
        finBuffer.close();
        fin.close();
        inicioBuffer.flush();
        inicioBuffer.close();
        inicio.close();
        fatalBuffer.flush();
        fatalBuffer.close();
        errores.close();
    }
    
    def buscarPorFecha(logs, i){
        for(File log: logs){
            System.out.println("Procesando fichero: "+log.name);
            log.eachLine{
                line -> esAntesODespues(line, i);
            }
        }
    }
    
    def esAntesODespues(str, fechaActual){
        if(str.contains(fechaActual) && str.contains("FATAL")){
           errores.write("\n${str}"); 
        } else if(str.contains(fechaActual) && str.contains("Inicio de")){
            parseLineInicio(str, fechaActual);
        }else if(str.contains(fechaActual) && str.contains("Fin de")){
            parseLineFin(str, fechaActual);
        }
    }
    
    def parseLineInicio(str, fechaActual){
    
        def minutoTraza = str.substring(str.indexOf(fechaActual)+fechaActual.length(),str.indexOf(fechaActual)+fechaActual.length()+2);
        if(minutoTraza.toInteger()>=minutoInicio-margen && minutoTraza.toInteger()<=minutoInicio)
        {
            inicio.write("\n${str}");
        }
    }
    
    def parseLineFin(str, fechaActual){
    
        def minutoTraza = str.substring(str.indexOf(fechaActual)+fechaActual.length(),str.indexOf(fechaActual)+fechaActual.length()+2);
        if(minutoTraza.toInteger()<minutoFin+margen && minutoTraza.toInteger()>=minutoFin)
        {
            fin.write("\n${str}");
        }
    }
    
}
println "Buscando:"
new Buscador().buscarAntesYDespues()
println "end"