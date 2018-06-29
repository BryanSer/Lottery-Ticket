// @Name 30选5
// @Usage §6[选购的数字]: 5个大于0小于等于30的数字 用,分割
// @Usage §b例如: 1,9,20,19,5 | 6,30,8,25,21
// @Code ThirtyPickFive
// @Default Price 5
// @Default Interval 1天
// @Default Enable true
// @Type String
// @Command 30选5,30p5,ThirtyPickFive

var WinAward = [8, 18, 30, 110, 600];
var Random = new java.util.Random();
var Utils = Java.type('Br.LotteryTicket.Utils');

/**
 * 初始化函数
 * @returns {undefined} 无返回值
 */
function init() {
}
/**
 * 判断用户输入的参数是否合法
 * @param {int|String} input 用户输入的参数
 * @returns {Boolean} 合法时返回true
 */
function isValid(input) {
    var str = input.split(',');
    if (str.length === 5) {
        for each(var o in str) {
            var i = parseInt(o);
            if (i > 0 && i <= 30) {
            } else {
                return false;
            }
        }
        return true;
    }
    return false;
}
var LastResult;
/**
 * 开奖时调用
 * @returns {String} 返回开奖结果(需要广播的)
 */
function Lottery() {
    var result = [0, 0, 0, 0, 0];
    for (var i = 0; i < result.length; i++) {
        result[i] = Random.nextInt(29) + 1;
    }
    LastResult = toStrings(result);
    return '&6&l  {三十选五} [%times%]期已开奖, 开奖结果为: %result%';
}

function toStrings(i) {
    var s = "";
    for(var o in i) {
        s += o + ',';
    }
    return s;
}

function toIntArray(s) {
    var i = new Array(s.length());
    var o = 0;
    for(var m in s.split(',')) {
        i[o++] = parseInt(m);
    }
    return i;
}

/**
 * 返回最后一次开奖的结果
 * @returns {int|String} 可以返回两者之一
 */
function getLastResult() {
    return LastResult;
}
var WinAward = [5.0, 10.0, 20.0];
/**
 * 读取配置 同时也可以在此写入配置
 * @param {FileConfiguration} config 
 * @returns {undefined} 无返回值
 */
function loadConfig(config) {
    if (!config.contains('WinAward.1')) {
        config.set('WinAward.1', WinAward[0]);
        config.set('WinAward.2', WinAward[1]);
        config.set('WinAward.3', WinAward[2]);
        config.set('WinAward.4', WinAward[3]);
        config.set('WinAward.5', WinAward[4]);
    } else {
        WinAward[0] = config.getDouble('WinAward.1');
        WinAward[1] = config.getDouble('WinAward.2');
        WinAward[2] = config.getDouble('WinAward.3');
        WinAward[3] = config.getDouble('WinAward.4');
        WinAward[4] = config.getDouble('WinAward.5');
    }
}

/**
 * 奖励玩家的方式
 * @param {Player} p 玩家
 * @param {Ticker} ticket 玩家所使用的票
 * @param {Result} result 开奖结果
 * @returns {undefined} 无返回值
 */
function onAward(p, ticket, result) {
    var t = toIntArray(ticket.getResult());
    var re = toIntArray(r.getResult());
    var a = 0;
    for (var index = 0; index < re.length; index++) {
        var e = re[index];
        if (e === t[index]) {
            a++;
        }
        if (index > 5) {
            break;
        }
    }
    switch (a) {
        case 0:
            p.sendMessage(Utils.sendMessage("&6&l可惜 你没有获得任何奖励QAQ"));
            break;
        case 1:
            Utils.payToPlayer(p, WinAward[0] * ticket.getAmount());
            p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了一个数字 获得: " + WinAward[0] * ticket.getAmount()));
            break;
        case 2:
            Utils.payToPlayer(p, WinAward[1] * ticket.getAmount());
            p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了二个数字 获得: " + WinAward[1] * ticket.getAmount()));
            break;
        case 3:
            Utils.payToPlayer(p, WinAward[2] * ticket.getAmount());
            p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了三个数字 获得: " + WinAward[2] * ticket.getAmount()));
            break;
        case 4:
            Utils.payToPlayer(p, WinAward[2] * ticket.getAmount());
            p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了四个数字 获得: " + WinAward[3] * ticket.getAmount()));
            break;
        case 5:
            Utils.payToPlayer(p, WinAward[2] * ticket.getAmount());
            p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了五个数字 获得: " + WinAward[4] * ticket.getAmount()));
            break;
    }
}