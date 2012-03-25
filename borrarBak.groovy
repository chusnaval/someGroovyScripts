def directory = new File("D:\\wks");

def deleteBaksClosure =
{
    if (it.name.endsWith(".bak"))
    {
        println "Deleting ${it.name}"
        it.delete()
    }
}

println "Matching Files:"
directory.eachFileRecurse(deleteBaksClosure)
println "end" 