// @Name 福彩3D
// @Usage §6[选购的数字]: 连续的§l3§6个数字 注意 首项不能为0
// @Usage §b例如 : 156 | 559
// @Code Welfare3D
// @Default Price 5
// @Default Interval 1天
// @Default Enable true
// @Type Int
// @Command 福彩3D,3D,福利彩票3D,Welfare3D
var Random = new java.util.Random();
var Utils = Java.type('Br.LotteryTicket.Utils');
var LastResult;
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
    if (input > 999 || input < 100) {
        return false;
    }
    return true;
}

/**
 * 开奖时调用
 * 其中返回值里 支持颜色转意
 * %times%表示期数
 * %result%表示结果
 * @returns {String} 返回开奖结果(需要广播的)
 */
function Lottery() {
    var i = 0;
    while (i < 99) {
        var v = Random.nextInt(9);
        i = i * 10 + v;
    }
    LastResult = i;
    return '&e&l{福彩3D} 第[%times%] 期彩票已经开奖 结果为: %result% &b请有购买的拿起彩票右键领取';
}
/**
 * 返回最后一次开奖的结果
 * @returns int|String 可以返回两者之一
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
    } else {
        WinAward[0] = config.getDouble('WinAward.1');
        WinAward[1] = config.getDouble('WinAward.2');
        WinAward[2] = config.getDouble('WinAward.3');
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
    var t = Utils.toIntArray(ticket.getNumber());
    var re = Utils.toIntArray(r.getNumber());
    var a = 0;
    for (var index = 0; index < re.length; index++) {
        var e = re[index];
        if (e === t[index]) {
            a++;
        }
        if (index > 2) {
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
    }
}
