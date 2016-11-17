/**
 * Created by aruis on 16-11-17.
 */
class Application {
    static def list = []

    public static void main(String[] args) {

        def file = System.getProperty('file')

        if (file == null)
            file = 'D:\\file.xls'

        new ExcelBuilder(file).eachLine([labels: true]) {

            def map = [id: 用户名, no: 份数.toInteger(), pid: 推荐人]

            list << map
        }

        def result = ''
        list.each {
            def _children = getChildren(it)
            it.total = 0
            def _childrenStr = ''
            _children.each { child ->
                it.total += child.no
                _childrenStr += child.id + ','
            }

            result += "$it.id,$it.total,$_childrenStr"
            result += '\n'
        }

        new File('D:\\result.csv') << result
    }


    static List getChildren(item) {
        def result = []
        def children = list.findAll { it.pid == item.id }

        result << children
        children.each {
            if(it == item){
                new File('D:\\error.txt') << "$it 数据有误"
            }else{
                result << getChildren(it)
            }

        }

        return result.flatten()
    }

}
