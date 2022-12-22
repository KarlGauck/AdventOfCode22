import java.lang.Integer.max
import kotlin.math.min

object Day19 {

    fun readBlueprints(src: String): MutableList<Blueprint> {
        val lines = Utils.getLines(src)
        val blueprints = mutableListOf<Blueprint>()
        for (line in lines) {
            val blueprint = Blueprint(index=line.split(":")[0].split(" ")[1].toInt())
            for (costString in line.split(".").filter { it != "" }) {
                val cost = Cost()
                costString.split(" and ").forEach {
                    val resourceCost = it.split(" ")[":-1"].toInt()
                    when (it.split(" ").last()) {
                        "ore" -> cost.ore = resourceCost
                        "clay" -> cost.clay = resourceCost
                        "obsidian" -> cost.obsidian = resourceCost
                    }
                }
                when (costString.split("Each ")[1].split(" robot")[0]) {
                    "ore" -> blueprint.orecost = cost
                    "clay" -> blueprint.claycost = cost
                    "obsidian" -> blueprint.obsidiancost = cost
                    "geode" -> blueprint.geodecost = cost
                }
            }
            blueprints += blueprint
        }
        return blueprints
    }

    fun part1() {
        val blueprints = readBlueprints("19")
        var configs = blueprints.map { Config(it) }.toMutableList()
        var max = 0
        fun tick(conf: Config, time: Int = 0): Int {
            if ((1..23-time).fold(conf.open) {r, it -> r + conf.geoderobs+it} < max)
                return -1
            max = max(max, conf.open)
            var transactions = ROBOT.values().filter { conf.canAfford(it) }
            if (ROBOT.GEODE in transactions)
                transactions = listOf(ROBOT.GEODE)
            conf.work()
            val res = mutableListOf<Int>()
            if(time < 23) {
                if (transactions.isNotEmpty())
                    res += transactions.maxOf {
                        val nConf = conf.clone()
                        nConf.buy(it)
                        tick(nConf, time+1)
                    }
                res += tick(conf, time+1)
            } else
                res += conf.open
            return res.max()
        }
        println(configs.foldIndexed(0) {i, r, it -> max=0;val t = tick(it);println("$i: -> $t"); r + t*(i+1) })
    }

    fun part2Recursive() {
        val allLines = readBlueprints("19")
        val blueprints = allLines.subList(0, min(allLines.size,3))

        val maxTime = 31

        var configs = blueprints.map { Config(it) }.toMutableList()
        var max = 0

        fun tick(conf: Config, time: Int = 0): Int {

            // calculate max amount of resources (geodes)
            val maxore = (1..maxTime-time).fold(conf.ore) {r, it -> r + conf.orerobs+it}
            val maxclayrobs = conf.clayrobs + maxore/conf.blueprint.claycost.ore
            val maxclay = (1..maxTime-time).fold(conf.clay) { r, it -> r + min(conf.clayrobs+it,maxclayrobs)}
            val maxobsrobs = conf.obsrobs + min(maxore/conf.blueprint.obsidiancost.ore, maxclay/conf.blueprint.obsidiancost.clay)
            val maxobs = (1..maxTime-time).fold(conf.obsidian) { r, it -> r + min(conf.obsrobs+it,maxobsrobs)}
            val maxgeoderobs = conf.geoderobs + min(maxore/conf.blueprint.geodecost.ore, maxobs/conf.blueprint.geodecost.obsidian)
            val maxgeode = (1..maxTime-time).fold(conf.open) { r, it -> r + min(conf.geoderobs+it,maxgeoderobs)}

            // stop if cant reach max achieved geodecount
            if (maxgeode <= max)
                return -1
            max = max(max, conf.open)

            if (time < 11)
                print("$max \r")

            // save if config can afford all (if yes, something has to be bought)
            var canAffordAll = ROBOT.values().all { conf.canAfford(it) }

            // setup all possible transactions
            var transactions = ROBOT.values().filter { conf.canAfford(it) }
            if (ROBOT.GEODE in transactions)
                transactions = listOf(ROBOT.GEODE)

            // start getting new resources
            conf.work()

            val res = mutableListOf<Int>()
            if(time < maxTime) {
                if (transactions.isNotEmpty())
                    res += transactions.maxOf {
                        val nConf = conf.clone()
                        nConf.buy(it)

                        // if enough resource to buy anything, don't buy this robot
                        if (conf.resource(it) >= conf.maxCost(it)*(maxTime-time) && it != ROBOT.GEODE)
                            -1
                        else
                            tick(nConf, time+1)
                    }

                // if everything is affordable, then buy!
                if (!canAffordAll)
                    res += tick(conf.clone(), time+1)
            } else
                res += conf.open
            return res.max()
        }
        println(configs.foldIndexed(1) {i, r, it -> max=0;val t = tick(it);println("$i: -> $t"); r*t })
        // 10 * 13 * 21
    }


    fun part2Iterative() {
        val allLines = readBlueprints("19")
        val input = allLines.subList(0, min(allLines.size, 3))
        var blueprints = input.map { Config(it) }.toMutableList()
        val maxTime = 31

        for (print in blueprints) {

            var max = 0
            val configs = mutableListOf(print.clone())
            var lastPrint = System.currentTimeMillis()
            while(configs.isNotEmpty()) {
                val conf = configs.maxBy { it.open*4*it.time + it.obsidian*5 + it.clay*2 + it.ore + (maxTime-it.time)*
                            (it.obsrobs*5 + it.clayrobs*2 + it.orerobs + it.geoderobs*4*it.time) }
                configs.remove(conf)
                if (System.currentTimeMillis()-lastPrint > 300) {
                    lastPrint = System.currentTimeMillis()
                    print("$max -> ${configs.size}: ${conf.time} ${if (configs.isNotEmpty()) configs.maxOf { it.time } else ""}\r")
                }

                // setup all possible transactions
                var transactions = ROBOT.values().filter { conf.canAfford(it) }
                if (ROBOT.GEODE in transactions)
                    transactions = listOf(ROBOT.GEODE)

                // start getting new resources
                conf.work()

                // keep track of highest geode-count, found yet
                max = max(max, conf.open)

                // calculate max amount of resources (geodes)
                val maxore = (1..maxTime-conf.time).fold(conf.ore) {r, it -> r + conf.orerobs+it}
                val maxclayrobs = conf.clayrobs + maxore/conf.blueprint.claycost.ore
                val maxclay = (1..maxTime-conf.time).fold(conf.clay) { r, it -> r + min(conf.clayrobs+it,maxclayrobs)}
                val maxobsrobs = conf.obsrobs + min(maxore/conf.blueprint.obsidiancost.ore, maxclay/conf.blueprint.obsidiancost.clay)
                val maxobs = (1..maxTime-conf.time).fold(conf.obsidian) { r, it -> r + min(conf.obsrobs+it,maxobsrobs)}
                val maxgeoderobs = conf.geoderobs + min(maxore/conf.blueprint.geodecost.ore, maxobs/conf.blueprint.geodecost.obsidian)
                val maxgeode = (1..maxTime-conf.time).fold(conf.open) { r, it -> r + min(conf.geoderobs+it,maxgeoderobs)}

                // if config with higher open geodes found, return
                if (maxgeode < max)
                  continue

                // if time is out, return
                if (conf.time == maxTime)
                    continue

                for (ac in transactions) {
                    if(conf.resource(ac) >= conf.maxCost(ac)*(maxTime-conf.time) && ac != ROBOT.GEODE)
                        continue
                    val newConf = conf.clone()
                    newConf.time = conf.time + 1
                    newConf.buy(ac)
                    configs += newConf
                }

                val newConf = conf.clone()
                newConf.time = conf.time + 1
                configs += newConf
            }
            println("-------> $max")
        }
    }

    enum class ROBOT {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    class Config (
        var blueprint: Blueprint,
        var orerobs: Int = 1,
        var clayrobs: Int = 0,
        var obsrobs: Int = 0,
        var geoderobs: Int = 0,
        var ore: Int = 0,
        var clay: Int = 0,
        var obsidian: Int = 0,
        var open: Int = 0,
        var time: Int = 0
    ) {
        fun canAfford(rob: ROBOT): Boolean {
            var cost = when (rob) {
                ROBOT.ORE -> blueprint.orecost
                ROBOT.CLAY -> blueprint.claycost
                ROBOT.OBSIDIAN -> blueprint.obsidiancost
                ROBOT.GEODE -> blueprint.geodecost
            }
            return cost.clay <= clay && cost.ore <= ore && cost.obsidian <= obsidian
        }

        fun resource(rob: ROBOT): Int = when (rob)  {
            ROBOT.ORE -> ore
            ROBOT.CLAY -> clay
            ROBOT.OBSIDIAN -> obsidian
            ROBOT.GEODE -> open
        }

        fun robots(rob: ROBOT) : Int = when (rob) {
            ROBOT.ORE -> orerobs
            ROBOT.CLAY -> clayrobs
            ROBOT.OBSIDIAN -> obsrobs
            ROBOT.GEODE -> geoderobs
        }

        fun maxCost(rob: ROBOT): Int = when(rob) {
            ROBOT.ORE -> listOf(blueprint.orecost.ore, blueprint.claycost.ore, blueprint.obsidiancost.ore, blueprint.geodecost.ore).max()
            ROBOT.CLAY -> listOf(blueprint.orecost.clay, blueprint.claycost.clay, blueprint.obsidiancost.clay, blueprint.geodecost.clay).max()
            ROBOT.OBSIDIAN -> listOf(blueprint.orecost.obsidian, blueprint.claycost.obsidian, blueprint.obsidiancost.obsidian, blueprint.geodecost.obsidian).max()
            else -> -1
        }

        fun buy(rob: ROBOT) {
            if (!canAfford(rob)) return
            var cost = Cost()
            when (rob) {
                ROBOT.ORE -> {
                    cost = blueprint.orecost
                    orerobs++
                }
                ROBOT.CLAY -> {
                    cost = blueprint.claycost
                    clayrobs++
                }
                ROBOT.OBSIDIAN -> {
                    cost = blueprint.obsidiancost
                    obsrobs++
                }
                ROBOT.GEODE -> {
                    cost = blueprint.geodecost
                    geoderobs++
                }
            }
            ore -= cost.ore
            clay -= cost.clay
            obsidian -= cost.obsidian
        }

        fun work() {
            ore += orerobs
            clay += clayrobs
            obsidian += obsrobs
            open += geoderobs
        }

        fun clone(): Config = Config(blueprint.clone(), orerobs, clayrobs, obsrobs, geoderobs, ore, clay, obsidian, open)
    }

    class Blueprint (
        var index: Int = 0,
        var orecost: Cost = Cost(),
        var claycost: Cost = Cost(),
        var obsidiancost: Cost = Cost(),
        var geodecost: Cost = Cost()
    ) {
        fun clone(): Blueprint = Blueprint(index, orecost.clone(), claycost.clone(), obsidiancost.clone(), geodecost.clone())
    }

    class Cost(
        var clay: Int = 0,
        var ore: Int = 0,
        var obsidian: Int = 0
    ) {
        fun clone(): Cost = Cost(clay, ore, obsidian)
    }
}