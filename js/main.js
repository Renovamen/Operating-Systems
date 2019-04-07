(function (window) 
{
    var document = window.document;

    // 获取“开始”按钮
    var startBtn = document.getElementById("startBtn");

    var memoryBlockNumber = 4; //内存块数
    var instructionNumber = 320; //总指令数
    var numberOfInstructionsInEachPage = 10; //每页存放指令数

    // 需要改变的标签元素
    var currentInstructionSpan = document.getElementById("currentInstruction");
    var numberOfMissingPagesSpan = document.getElementById("numberOfMissingPages");
    var pageFaultRateSpan = document.getElementById("pageFaultRate");

    var memory = []; // 内存
    var instructions = []; // 记录指令是否被执行
    var insCount = 0; // 记录执行的指令个数
    var missingPage = 0; // 缺页个数

    function flagInstructionExecuted(ins) 
    {
        if (typeof instructions[ins] === "undefined") 
        {
            instructions[ins] = false;
        };
        return instructions[ins];
    };

    function flagInstructionAvailable(ins) 
    {
        for (var i = 0; i < memory.length; i++) 
        {
            // 已经存在，没有发生缺页
            if (Math.floor(ins / numberOfInstructionsInEachPage) === memory[i]) return i+1;
        };
        // 缺页
        return false;
    };

    function Pre() 
    {
        memory = new Array(memoryBlockNumber);
        instructions = new Array(instructionNumber);
        insCount = 0;
        missingPage = 0;

        currentInstructionSpan.textContent = -1;
        numberOfMissingPagesSpan.textContent = missingPage;
        pageFaultRateSpan.textContent = missingPage / 320; 
    };

    function FIFO() 
    {
        // 选择指令的策略
        //  0 - 顺序执行
        //  1 - 向后跳转
        // -1 - 向前跳转
        var strategy = 1;

        var po = 0;

        var instruct = -1;
        while(insCount < 320) 
        {

            // 选择运行的指令
            if (strategy === 0) 
            {
                // 顺序执行
                instruct++;

                // 更新策略
                if (insCount % 4 === 1) 
                {
                    // 向前跳转
                    strategy = -1;
                } 
                else 
                {
                    // 向后跳转
                    strategy = 1;
                };
            } 
            else if (strategy === 1) 
            {
                // 向后跳转
                if (instruct + 1 > 319) 
                {
                    strategy = -1;
                    continue;
                };

                instruct = Math.floor(Math.random() * (instructionNumber - (instruct + 1)) + (instruct + 1));

                // 更新策略
                // 顺序执行
                strategy = 0;
            } 
            else if (strategy === -1) 
            {
                // 向前跳转
                if (instruct - 2 < 0) 
                {
                    strategy = 1;
                    continue;
                };

                instruct = Math.floor(Math.random() * (instruct - 1));

                // 更新策略
                // 顺序执行
                strategy = 0;
            };

            // 处理越界
            if (instruct < 0) 
            {
                // 向下越界
                instruct = -1;
                
                // 更新策略
                // 向后跳转
                strategy = 1;

                continue;
            } 
            else if (instruct >= 320) 
            {
                // 向上越界
                instruct = 321
                
                // 更新策略
                // 向前跳转
                strategy = -1;

                continue;
            };

            var flagNotInBlock = 0;
            // 判断选中的指令是否被运行过
            if (!flagInstructionExecuted(instruct)) 
            {
                // 当前指令没有被运行过
                // 更新相应html标签
                currentInstructionSpan.textContent = instruct;
                
                // 判断选中指令是否在内存中
                flagInBlock = flagInstructionAvailable(instruct);
                // 不在内存中
                if (!flagInBlock)
                {
                    // 缺页
                    missingPage++;
                    // 更新相应html标签
                    numberOfMissingPagesSpan.textContent = missingPage;
                    pageFaultRateSpan.textContent = missingPage / 320;
                    memory[(po++) % 4] = Math.floor(instruct / numberOfInstructionsInEachPage);
                };
                insCount++;
                instructions[instruct] = true;
            };

            var row = document.getElementById("memoryTable").insertRow();
            row.insertCell(0).innerHTML = instruct;
            row.insertCell(1).innerHTML = memory[0];
            row.insertCell(2).innerHTML = memory[1] == undefined ? "Empty" : memory[1];
            row.insertCell(3).innerHTML = memory[2] == undefined ? "Empty" : memory[2];
            row.insertCell(4).innerHTML = memory[3] == undefined ? "Empty" : memory[3];
            // 不在内存中 替换
            if(flagInBlock == false)row.insertCell(5).innerHTML = "发生缺页，指令" + instruct + "不在内存中，" + "将指令" + instruct + "所在的页调入内存，替换块" + (po % 4 + 1);
            // 在内存中 输出相应信息
            else row.insertCell(5).innerHTML = "指令" + instruct + "在内存块" + flagInBlock + "中";
        };
    };

    function LRU() 
    {
        // 选择指令的策略
        //  0 - 顺序执行
        //  1 - 向后跳转
        // -1 - 向前跳转
        var strategy = 1;

        // 访问顺序，靠近末尾的为最近访问的
        var stack = [0, 1, 2, 3];

        var instruct = -1;
        while(insCount < 320) 
        {
            // 选择运行的指令
            if (strategy === 0) 
            {
                // 顺序执行
                instruct++;

                // 更新策略
                if (insCount % 4 === 1) 
                {
                    // 向前跳转
                    strategy = -1;
                } 
                else 
                {
                    // 向后跳转
                    strategy = 1;
                };
            } 
            else if (strategy === 1) 
            {
                // 向后跳转
                if (instruct + 1 > 319) 
                {
                    strategy = -1;
                    continue;
                };

                instruct = Math.floor(Math.random() * (instructionNumber - (instruct + 1)) + (instruct + 1));

                // 更新策略
                // 顺序执行
                strategy = 0;
            } 
            else if (strategy === -1) 
            {
                // 向前跳转
                if (instruct - 2 < 0) 
                {
                    strategy = 1;
                    continue;
                };

                instruct = Math.floor(Math.random() * (instruct - 1));

                // 更新策略
                // 顺序执行
                strategy = 0;
            };

            // 处理越界
            if (instruct < 0) 
            {
                // 向下越界
                instruct = -1;
                
                // 更新策略
                // 向后跳转
                strategy = 1;

                continue;
            } 
            else if (instruct >= 320) 
            {
                // 向上越界
                instruct = 321
                
                // 更新策略
                // 向前跳转
                strategy = -1;

                continue;
            };

            var flagInBlock = 0;
            // 判断选中的指令是否被运行过
            if (!flagInstructionExecuted(instruct)) 
            {
                // 当前指令没有被运行过
                // 更新相应html标签
                currentInstructionSpan.textContent = instruct;
                
                // 判断选中指令是否在内存中
                flagInBlock = flagInstructionAvailable(instruct);
                // 不在内存中
                if (!flagInBlock) 
                {
                    // 缺页
                    missingPage++;
                    // 更新相应html标签
                    numberOfMissingPagesSpan.textContent = missingPage;
                    pageFaultRateSpan.textContent = missingPage / 320;
                    memory[stack[0]] = Math.floor(instruct / numberOfInstructionsInEachPage);
                };

                // 更新访问顺序
                var page = Math.floor(instruct / numberOfInstructionsInEachPage);
                var block = memory.indexOf(page);

                // 将当前块在访问顺序数组中挪到最后一位
                stack.splice(stack.indexOf(block), 1);
                stack.push(block);

                insCount++;
                instructions[instruct] = true;
            };

            var row = document.getElementById("memoryTable").insertRow();
            row.insertCell(0).innerHTML = instruct;
            row.insertCell(1).innerHTML = memory[0];
            row.insertCell(2).innerHTML = memory[1] == undefined ? "Empty" : memory[1];
            row.insertCell(3).innerHTML = memory[2] == undefined ? "Empty" : memory[2];
            row.insertCell(4).innerHTML = memory[3] == undefined ? "Empty" : memory[3];
            // 不在内存中 替换
            if(flagInBlock == false)row.insertCell(5).innerHTML = "发生缺页，指令" + instruct + "不在内存中，" + "将指令" + instruct + "所在的页调入内存，替换块" + (stack[0] + 1);
            // 在内存中 输出相应信息
            else row.insertCell(5).innerHTML = "指令" + instruct + "在内存块" + flagInBlock + "中";
        };
    };

    function chooseAlgrithm() 
    {
        var ratio = document.querySelector("input:checked");
        if (ratio.value === "FIFO") 
        {
            FIFO();
        } 
        else if(ratio.value === "LRU") 
        {
            LRU();
        } 
    };

    function start() 
    {
        // 禁用“开始”按钮
        startBtn.disabled = true;

        // 初始化变量
        Pre();

        $("#memoryTable  tr:not(:first)").empty("");

        // 选择算法并开始
        chooseAlgrithm();



        // 启用“开始”按钮
        startBtn.disabled = false;
    }

    // 添加开始按钮的监听事件
    startBtn.addEventListener('click', start);

    
})(window)

