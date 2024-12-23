<?php

    ini_set('memory_limit', '350M');

    $numbers = explode("\n", input());


    $profit_by_pattern = [];
    
    $total = 0;
    foreach ($numbers as $initial_num) {
        $num = (int)$initial_num;
        
        $prev0 = PHP_INT_MIN;
        $prev1 = PHP_INT_MIN;
        $prev2 = PHP_INT_MIN;
        $prev3 = PHP_INT_MIN;
        
        
        for ($i =0; $i < 2000; $i++) {
            //mix with times 64 and then prune
            $mix = $num*64;
            $num = $num ^ $mix;
            $num = $num % 16777216;
            
            //mix with divided by 32 and then prune
            $mix = (int)($num/32);
            $num = $num ^ $mix;
            $num = $num % 16777216;
            
            //mix with times2048 and then prune
            $mix = $num * 2048;
            $num = $num ^ $mix;
            $num = $num % 16777216;
            
            $price = $num % 10;
            
            if ($prev3 > PHP_INT_MIN) {
                $key = $prev2 - $prev3  . ',' . $prev1 - $prev2. ',' . $prev0 - $prev1 . ',' . $price - $prev0;
                if (($profit_by_pattern[$initial_num][$key] ?? PHP_INT_MIN) == PHP_INT_MIN)
                    $profit_by_pattern[$initial_num][$key] = $price;
            }
            
            $prev3 = $prev2;
            $prev2 = $prev1;
            $prev1 = $prev0;
            $prev0 = $price;
        }
        
        
        $total += $num;
    }
    
    $final_profit = [];
    foreach ($numbers as $initial_num) {
        foreach ($profit_by_pattern[$initial_num] as $pattern => $profit) {
            $final_profit[$pattern] ??= 0;
            $final_profit[$pattern] += $profit;
        }
    }
    
    $max_profit = PHP_INT_MIN;
    foreach (array_values($final_profit) as $p) {
        $max_profit = max($p, $max_profit);
    }
    
    var_dump($max_profit);
    
    
	
function sample() {
	return <<<EOD
    1
    2
    3
    2024
    EOD;	
}

function input() {
	return <<<EOD
    16497617
    11602566
    4165644
    390270
    10795222
    715823
    8266514
    13933887
    1451442
    4718098
    372336
    14629042
    6366176
    1189706
    1141421
    9915158
    8836022
    12357164
    545954
    11026151
    7664901
    11505993
    2766360
    3380869
    3842790
    10864264
    10027097
    11279149
    4562307
    8451233
    3645713
    11675068
    5498602
    12521050
    1855802
    5545736
    153969
    457092
    2394379
    5229562
    5129714
    9551752
    16696889
    8479500
    9897495
    4301720
    15427334
    5642865
    7285262
    693542
    3373016
    5572041
    11460954
    11220273
    3120677
    7945504
    9456418
    13429798
    14986714
    10076857
    7359251
    16692692
    12847408
    11806513
    15506436
    12567213
    5833293
    2986382
    417985
    2679575
    4529356
    4093927
    11541735
    7144433
    9799550
    11352998
    15563605
    10980512
    4288418
    15406758
    4373682
    1245213
    12327356
    12796650
    15349398
    16401755
    15197483
    10963492
    15960660
    10613006
    14985597
    201456
    15972563
    12971427
    12129118
    2884683
    8126145
    5381738
    6610381
    3326630
    1012070
    2024320
    14403338
    6046174
    13836189
    4363507
    10547859
    7711312
    15102914
    14393261
    13682177
    16626279
    9282128
    6846088
    9644445
    3161896
    3077246
    12529542
    1317343
    7103100
    4918913
    1288629
    7244017
    1966958
    4144436
    284061
    7621039
    2545640
    1058468
    5710157
    15725673
    5957419
    3976068
    4006930
    652303
    14677187
    7378104
    4003310
    5370881
    5649285
    720686
    9149790
    10368822
    11078663
    1900744
    12213473
    13216749
    14157779
    9722505
    10113474
    14434159
    5167020
    1789522
    7309632
    4894430
    6524258
    12118076
    7735549
    7483495
    1275347
    3982638
    9090508
    16082554
    873491
    12720280
    741765
    13592823
    276504
    9658896
    9680744
    11850122
    16435130
    13162607
    15615957
    5758119
    16639368
    8155715
    8391063
    3295944
    6118261
    9044662
    3674650
    649652
    9770326
    11597238
    1837042
    6745820
    6022618
    2792933
    3433940
    2993595
    7372233
    2361850
    14809351
    265082
    5158450
    3159388
    6813345
    12478186
    14841989
    13686864
    4526204
    11322655
    12527785
    7786168
    8316636
    12839623
    9409190
    11020371
    11329524
    7918693
    6548642
    3775084
    11680833
    2860917
    13747402
    8769655
    15380376
    11329396
    6409742
    6131317
    15266869
    3276683
    13165703
    14140632
    3926193
    5277569
    9281547
    2070939
    13802734
    14818395
    5703269
    16350347
    11869763
    13157855
    5563549
    13044987
    6443547
    10980816
    7658723
    5164302
    16404753
    5810075
    1964736
    10893652
    6006078
    8141472
    9610899
    6107869
    11718132
    317978
    14547374
    10816735
    1790819
    15482650
    7336664
    11257319
    13858559
    2215616
    7990684
    14663188
    4961117
    10118212
    1927765
    13553633
    13199682
    9338643
    2942909
    7094080
    1868090
    10146946
    6282645
    3809830
    3719863
    4656728
    1720533
    153145
    5575958
    13147784
    11739649
    11460677
    8113447
    16165162
    7965484
    10994456
    14232825
    12022483
    8370847
    13925305
    1574030
    15406849
    7601703
    9429073
    5061363
    12571487
    15986617
    3740947
    14700756
    10777895
    1155928
    3577347
    15966740
    6465757
    3132910
    6118926
    2514006
    3442205
    15809108
    10574301
    9598586
    16243743
    14387890
    13594556
    15348258
    9364450
    12101129
    16663675
    8728092
    9711100
    10944993
    16437091
    3096664
    13252381
    1360897
    1901703
    7289209
    11342692
    1926481
    8131620
    4357272
    7231658
    7523125
    5351313
    10597600
    3921629
    9030923
    16664331
    6601193
    13629053
    4898278
    3788763
    11750516
    11250737
    10361894
    5582498
    5652748
    12974176
    10360610
    2752029
    16254624
    2801271
    3384279
    2506405
    15035799
    11891839
    4434524
    12588398
    1896995
    9866308
    11539389
    16112629
    7697370
    8947529
    15444185
    929634
    11120221
    10625338
    16489474
    8784985
    15500520
    14341527
    10677818
    11875714
    3763728
    7792549
    12102468
    4096317
    16730036
    3531676
    11604728
    4949295
    12742203
    7923836
    14575603
    8532990
    5967800
    507544
    7033109
    7283233
    15066178
    12671848
    7531022
    13926877
    809733
    11909831
    8062166
    11971446
    335873
    1336348
    2052071
    6610242
    9827117
    16333456
    8749262
    5400238
    10029067
    3327410
    265705
    6340957
    691704
    6002535
    395778
    4408154
    13709228
    12857431
    15552242
    10067145
    16055362
    12867682
    3808894
    11188725
    16486376
    1349865
    7856207
    5103432
    5232392
    11527680
    15312409
    382899
    7824927
    4376031
    3865055
    11091516
    2436073
    10018325
    8411835
    6006951
    11723900
    15067171
    7878769
    9476852
    8842292
    1642084
    11375348
    9142548
    2243048
    3912225
    349668
    6012253
    3658439
    3862489
    7972652
    5662298
    3604638
    7858893
    3575828
    14306642
    6821573
    6504414
    16279345
    9570108
    3496986
    4978123
    16467242
    11410437
    7180935
    5638351
    8160130
    2446075
    1833091
    13645194
    15470922
    976559
    4851629
    13307398
    3173134
    7523847
    14923591
    13666176
    4763449
    9257766
    13228745
    5448567
    8514496
    15418038
    4529669
    10511942
    8127235
    16342630
    6554239
    1211483
    11461025
    15490002
    3184266
    6322384
    10750764
    6855869
    194462
    7434215
    14970330
    11277916
    12924916
    16548228
    15071863
    11469466
    13776126
    10327690
    6261861
    1951585
    11152048
    14269278
    4747726
    2004160
    6965165
    4355503
    12020917
    11645501
    7914124
    10534633
    13285218
    15046102
    11833668
    14889848
    6926117
    11852589
    11740016
    6882372
    4185470
    14200579
    15183836
    13369212
    2528887
    7594363
    9481983
    16712434
    1708322
    5351604
    15990913
    7471350
    3758643
    14498874
    299717
    14207915
    2407929
    13918230
    1106595
    15969900
    10949678
    5583827
    11245589
    14533616
    9951906
    14032246
    9913430
    6125116
    1917189
    4874841
    11718745
    2287261
    6522356
    2316053
    14848238
    15438625
    16480474
    418787
    2679269
    4298802
    598823
    344978
    10509503
    16763665
    2941728
    6270954
    2200333
    637202
    8280210
    15165052
    15800980
    13150857
    3208638
    7951331
    12761766
    14429039
    15514092
    4159806
    14993566
    3903920
    4869469
    9139986
    5961363
    3374637
    15760559
    12040678
    12833905
    8887388
    2218524
    11415256
    13071002
    6740320
    5200543
    8175859
    6321882
    10293604
    6175117
    1784425
    15770404
    1942715
    11451397
    15501004
    13335053
    1284435
    4777015
    12368958
    5251344
    6466455
    16214188
    9320674
    14204435
    4128730
    2937450
    2946849
    5885264
    12172768
    6116390
    3532347
    5103838
    2012955
    12741050
    15168558
    6032505
    5568655
    892779
    4448869
    16658678
    2209584
    6087142
    10536615
    4080610
    12516357
    12427094
    11535743
    16155433
    14854140
    13753714
    11100661
    13355934
    7692781
    15965468
    15771500
    3908900
    6129336
    9875412
    1287606
    7094747
    14073846
    16194354
    5397451
    2029680
    1584184
    5879904
    15819562
    8033769
    4681733
    16035523
    2624473
    14566061
    8535152
    13326053
    3780330
    4078219
    4469312
    10548022
    6886096
    13496706
    10558242
    740387
    1495350
    16234691
    11634825
    180948
    9730269
    8122924
    14386493
    841214
    5084868
    10573245
    11167423
    12760345
    7950423
    3067416
    15491567
    4949176
    2040667
    12560400
    8683129
    8972479
    7376964
    7929715
    9650641
    8692711
    4765483
    3068410
    1312876
    4216070
    13668046
    14508568
    14865984
    466842
    8099626
    13606849
    8026445
    6046544
    15037893
    4146901
    13790800
    8784720
    1472609
    14549740
    5180250
    4532126
    8434377
    5570659
    14367325
    15228464
    5074297
    281722
    2847053
    15455206
    5471290
    9224965
    6994606
    11081010
    1140081
    6431180
    5530356
    12253161
    10357635
    6512961
    1659186
    13351321
    12241356
    10449241
    8322858
    7931012
    6038754
    13422987
    9990595
    7882626
    6797851
    2008566
    4930157
    14802697
    16772174
    6126183
    10313723
    4234702
    1819196
    15204551
    9616587
    15488696
    2481801
    8192983
    15846673
    3210818
    4110381
    5266315
    4762985
    8192977
    14159109
    9821841
    7666606
    8303131
    13807051
    11902580
    2094420
    9117022
    3633848
    16372452
    9982800
    8496359
    10850907
    15396400
    16678626
    4450355
    13524848
    2734771
    11370523
    4044020
    4210492
    5505110
    13558369
    10659640
    13799124
    5704572
    2514854
    6460951
    12991124
    2104799
    14994495
    8874628
    9865117
    4392649
    11275969
    1798431
    490079
    3417298
    2438272
    15253596
    13260595
    15812876
    1208218
    5760958
    13754814
    10572764
    9058982
    3350678
    1549624
    6160439
    9859937
    11063860
    10293710
    3331058
    12788955
    3993725
    1602098
    611605
    7296449
    13494656
    9991935
    3132852
    2595976
    9384623
    9870297
    2982851
    15846471
    5152376
    15940398
    7692726
    5623330
    14375951
    12876091
    7586100
    4058915
    6527866
    14021742
    14357596
    10718561
    9067604
    9865285
    2186543
    12932165
    7855124
    8423388
    2697086
    5442211
    7188027
    15809992
    4493757
    12089683
    3400550
    10209093
    1897833
    9830834
    5716207
    3969198
    12788316
    12921966
    14772119
    2215966
    10332813
    6460839
    5108144
    15913049
    16535290
    6367049
    9815566
    6037893
    11383898
    16075059
    10715719
    8109544
    6270996
    7111559
    15450592
    9617090
    9889183
    2731514
    310862
    14661479
    9392883
    10425063
    14379802
    8684310
    12757501
    9404736
    13592284
    7015316
    10411617
    16775468
    3775316
    7473977
    12233631
    11138995
    14786980
    1131596
    6400473
    4598085
    15675706
    11946081
    1378289
    7140724
    13147659
    6249796
    2523780
    14981810
    1385075
    16511021
    8362348
    10345267
    2107464
    5796900
    16734982
    5255074
    15964319
    15961177
    12580407
    12414090
    8544280
    13010589
    5067900
    12146622
    6527936
    1502343
    625852
    5906266
    15953419
    674471
    4878482
    10256164
    4213207
    6433799
    2146530
    16506729
    3205128
    14947431
    8375854
    2641045
    1329412
    3824885
    12916246
    3144947
    9542397
    136439
    2593669
    9409851
    5242614
    7354757
    15420376
    5216372
    11969880
    13763737
    15243806
    6602787
    4793836
    6552549
    13556707
    6449503
    15872848
    5079662
    2724506
    13178567
    9606435
    9088122
    6587062
    6764806
    16171909
    8067940
    6924225
    12465085
    13205492
    1889783
    5072022
    1365611
    14655502
    3277145
    10725798
    7103479
    2493549
    10657282
    5503680
    6529840
    15215988
    7209769
    424812
    12739224
    6680504
    2474216
    6503920
    13182318
    11130541
    9850291
    1433477
    9919193
    14466060
    3071515
    1932529
    7488935
    11121323
    12182900
    4968405
    13471478
    5028243
    8577780
    7665496
    6949777
    7177589
    2793433
    15487156
    1336999
    4489448
    7680127
    3190304
    3393523
    16348268
    3713763
    5906221
    1833748
    10427292
    13981877
    12182860
    13935961
    15379135
    12151087
    1424496
    5667278
    5778560
    4718337
    9603900
    2069709
    12927128
    15110159
    8661464
    10283971
    11505397
    4294510
    7349225
    8091489
    4521233
    9702593
    12397180
    5705893
    3668524
    7524179
    5894052
    7356600
    11468924
    5389672
    12980403
    3740358
    11174521
    5515742
    2608786
    9083475
    2099723
    15892959
    5635954
    14670563
    1003697
    9390679
    4184976
    5524184
    13230439
    1445897
    8329467
    2866908
    11441337
    11773310
    16310945
    10375190
    946739
    12603051
    11752888
    5581260
    14915119
    271062
    12038299
    6174199
    15490338
    16695684
    2207687
    11775885
    9098078
    8173119
    11060031
    835631
    3745681
    3590306
    13562116
    901395
    2710633
    5024186
    2824800
    8681417
    4900925
    6888619
    14074652
    5106343
    10421717
    9240248
    15193165
    2955622
    10687786
    2623776
    10199675
    13510775
    1747809
    15818174
    3201324
    2440291
    4164655
    11398472
    5047959
    553335
    10586460
    4295383
    6234721
    4970135
    9377104
    16483283
    14054253
    334870
    6598738
    11748498
    468172
    13466612
    8500055
    16570580
    670131
    14037945
    4179043
    4903679
    1936867
    1270977
    13606092
    10923314
    15166621
    10762620
    9869050
    13196155
    9297271
    14311590
    2092238
    14044709
    2360738
    15146557
    14137162
    10833319
    9662268
    10086476
    2031835
    1679075
    1855536
    362336
    4641604
    6090093
    8929478
    1145542
    15838740
    15350290
    3115200
    4629325
    14069869
    14753129
    5057059
    8150920
    11451912
    600093
    13966375
    16381480
    368964
    8199316
    15423691
    13353244
    8755347
    4632333
    11019681
    6986429
    5941654
    3598734
    15246843
    9232481
    10986973
    604156
    9546616
    816919
    8323470
    4418912
    8379201
    8313724
    8767743
    15281597
    4959155
    8378307
    3232953
    1928980
    3088717
    1519437
    3299690
    4198363
    12405695
    9095228
    6813670
    14852352
    1683148
    7583121
    3946119
    4373640
    749432
    7426681
    6774290
    7002147
    2250330
    12348789
    4126377
    4598677
    13409666
    15242005
    414277
    14331230
    2673756
    2676702
    11585505
    1997847
    10578011
    1065757
    14674325
    2385925
    8218310
    6953998
    5004975
    15748553
    1414420
    10025961
    8633909
    8790150
    7778975
    11699450
    7619633
    1304277
    15723035
    14748942
    12046780
    8030608
    16745556
    14562035
    11598534
    2006546
    4518409
    4079583
    6713997
    12107158
    3744144
    16390398
    2278854
    8087906
    10096592
    7493494
    6925731
    12373511
    6147271
    13731953
    4482636
    11880251
    9831242
    1605576
    11875195
    4104470
    14308378
    12691764
    2741416
    7045636
    9858691
    2437558
    16458890
    2419316
    13586124
    11880523
    6349691
    8684385
    331190
    14439844
    13104875
    4504273
    3304922
    12607987
    7196430
    5510404
    11740515
    15376771
    11506851
    12272000
    10202632
    9025534
    10944763
    10454045
    1977191
    7295708
    12709702
    2288421
    15291117
    5742782
    12436889
    3351571
    3400081
    7108801
    15045436
    14878648
    16538682
    6030894
    15947352
    7021715
    5110151
    2098489
    2572936
    16749001
    6791143
    9586836
    4616568
    8230554
    16771971
    6344974
    4607044
    1937492
    4971888
    5399390
    6667591
    8573837
    11201214
    7512203
    1848241
    4709388
    7272398
    8867447
    6842966
    3162377
    3134464
    4786960
    5588769
    1822832
    5639515
    6758859
    7925191
    1549179
    10720317
    9890579
    3998853
    15048843
    4677185
    4586548
    11747884
    10541339
    11916977
    12496752
    693993
    5940925
    4185017
    3349141
    9364249
    8056440
    1690458
    16353945
    1529267
    13702753
    3199910
    14673752
    14200033
    1443091
    10848335
    16650132
    719847
    11138482
    6190549
    6624779
    6418323
    16350277
    12839370
    11685957
    11225923
    4994509
    6023547
    5511777
    13916617
    2438130
    4253285
    7078926
    10157181
    7085607
    14766883
    13727040
    3794244
    3512373
    16191084
    11182375
    10238827
    13963347
    12813500
    4091439
    11707640
    7236768
    11820717
    3122885
    8092516
    8054903
    4856557
    3201741
    15612281
    9307418
    962744
    12884348
    13774256
    14544463
    9405533
    12161437
    10602095
    3785238
    7005183
    3827414
    2478392
    16280078
    15259934
    15208656
    624912
    7945506
    2055350
    6359211
    13299816
    9142280
    14516706
    13745503
    3974434
    15440505
    1615588
    4084774
    3600231
    7992694
    817191
    10414625
    8020910
    11135924
    13757851
    16245752
    14355089
    3207220
    2777613
    5345816
    3535824
    9074478
    1214385
    7078073
    16421487
    4104921
    6878708
    4938946
    14468036
    7671615
    6429942
    13127151
    1069051
    5449209
    10283937
    15605402
    14400955
    9858022
    9558088
    841143
    5835134
    8683261
    543231
    758660
    2562417
    14790912
    1752184
    11194507
    8216019
    5375072
    596545
    2133106
    1771827
    914085
    14779242
    4078122
    9410910
    2027189
    12350073
    865508
    2707001
    4694217
    16478293
    10479630
    3065214
    521547
    5464932
    16740506
    14464509
    5851839
    10694560
    7563642
    10420422
    12866767
    14458507
    3120416
    7710612
    6431414
    5062710
    11566231
    15870929
    16747986
    10003301
    2440818
    2112907
    7278796
    10908611
    7821208
    3744758
    15370356
    14216113
    4082055
    6483548
    5045468
    2282703
    1927276
    13181601
    2529837
    5040293
    4643555
    15629074
    7749010
    897811
    1016042
    8938933
    16722435
    13872170
    14955264
    4505864
    13095670
    5289987
    305410
    11076262
    15032066
    971467
    1756613
    3154296
    11090417
    10467053
    8913897
    5288870
    6055012
    7384194
    12735939
    2782916
    11353604
    4287907
    16668853
    14060474
    4611638
    1358872
    12783481
    15926552
    5846456
    10394645
    13165176
    904170
    5646552
    6319668
    3482709
    11041128
    8949221
    9222855
    5836604
    2068850
    6712633
    5268181
    11727032
    11329372
    3436415
    7088537
    11258103
    2605611
    3371825
    7785823
    12592047
    8617015
    11313894
    7270844
    13551320
    9075210
    6187710
    11618521
    15080096
    9286197
    4692851
    14100304
    12138759
    7096817
    15639705
    4589471
    4222634
    3700104
    4705611
    6461581
    2155573
    8050092
    8348149
    2981667
    13706484
    13988195
    5642009
    16304616
    10057230
    4901976
    1540796
    8821231
    2514588
    12828984
    3678417
    8667167
    12418820
    3658767
    6252858
    6951828
    7192727
    15996830
    7739404
    13421205
    12652906
    9359128
    2287991
    14281648
    8918824
    3101183
    12266023
    9036269
    7178583
    5824109
    4000315
    4445883
    3404860
    11062670
    1934259
    671827
    3699812
    10597094
    1948922
    10435775
    13251412
    10180500
    3503199
    6593109
    11727779
    10886095
    10256793
    7778231
    16607570
    9455779
    16621037
    14864579
    8078811
    8277812
    8399458
    14929063
    741834
    147379
    9423958
    14882173
    4811658
    4728248
    3646460
    2962489
    1787687
    13764544
    7421108
    12144283
    14551451
    14709052
    8635597
    12628204
    11320918
    12875758
    1683475
    15762648
    381780
    6402848
    8959650
    4176535
    16554555
    13865919
    3452605
    13654965
    15456125
    7324786
    6065383
    9588840
    11925150
    13489766
    12834854
    3371832
    15339559
    8486860
    11143529
    6396922
    7259612
    1993164
    13370124
    10691761
    10455092
    13445694
    1994982
    15006155
    12601359
    14552906
    3623153
    3100032
    5524718
    7484921
    4169803
    9231523
    1768936
    14988703
    14278526
    7326768
    8730068
    15009566
    4084492
    1411880
    12403968
    3743040
    1235447
    4519404
    6180861
    5480553
    9943639
    6156360
    13977297
    15237504
    1217618
    16685991
    1601044
    14322454
    15541400
    14065822
    14438167
    14942872
    5546266
    1062894
    1507697
    1567419
    6456348
    3985546
    1374681
    8553009
    12491429
    14323659
    14873667
    6781814
    12919598
    5390364
    3994838
    2345311
    13512660
    488104
    901187
    12400164
    8176532
    8395652
    4737923
    2434220
    338915
    6117014
    4813051
    10191875
    7784125
    6518839
    4284528
    13641796
    16650347
    7491576
    4629518
    15550589
    13159298
    15364203
    3920441
    11535281
    3219514
    6051039
    15003014
    4822382
    8678872
    3729439
    12162506
    13939293
    13791507
    646692
    11946396
    13147052
    1606727
    2605139
    9997107
    5073720
    14795257
    6408592
    16742657
    8738597
    3626635
    6989479
    3259838
    14612412
    2255639
    13230613
    12404762
    1282907
    8312884
    2476287
    2644880
    11341746
    14205938
    10396784
    8377649
    1061506
    4321700
    2981477
    343178
    228577
    14603244
    15513648
    5172497
    1056537
    12603724
    9072591
    8293285
    16031526
    10488011
    1648262
    4294931
    9239879
    14229110
    5174510
    3133829
    16445186
    5965381
    16030152
    12082446
    11636414
    9451145
    9056966
    2833033
    11031081
    8087884
    12182040
    14563112
    14731647
    3366165
    1512227
    14777856
    3570474
    364825
    4496672
    4372234
    16244538
    13694605
    13788585
    9324503
    5186213
    4335526
    5124154
    3839342
    1499307
    9995526
    3957920
    4813158
    13348404
    9714344
    12404239
    153730
    9651627
    3157462
    7762225
    16232872
    12439168
    736685
    8434170
    5432606
    9793616
    10882673
    8791097
    2209937
    2474905
    9061744
    5504309
    11198542
    3292892
    10360417
    15518302
    11723971
    3623961
    14103565
    7861030
    4605978
    11922903
    4946873
    13634455
    192088
    15190298
    6120735
    9061324
    4525609
    1566988
    10727115
    10606547
    916744
    12113562
    11120761
    10405029
    8376851
    13481929
    10219936
    8305636
    7908183
    6716736
    14242174
    5306893
    15146236
    13626907
    8414952
    10255198
    10318868
    5060648
    10381519
    11232364
    7451498
    5306978
    11111384
    10565019
    5567207
    11685554
    8480079
    12291634
    1160655
    2671617
    9281486
    4420999
    4602903
    1734628
    829748
    657670
    6183880
    11492727
    13663241
    5592421
    13261661
    877162
    15152936
    8415756
    2038152
    11831723
    7458147
    4610364
    11257065
    9081288
    12196650
    2156221
    979569
    229822
    4424027
    1756178
    5123950
    5572282
    6724160
    3634482
    16490321
    10765861
    16287573
    2639262
    3729746
    14638686
    11336603
    3359825
    8127045
    10998629
    12240490
    3245997
    3884765
    10975532
    10417471
    13173194
    2631098
    10014757
    12081046
    16522197
    13386380
    14766152
    1666529
    12367180
    13154182
    6862853
    10335166
    3933094
    12571731
    6579517
    10837240
    10025446
    7771292
    12198882
    12883015
    5334797
    12574719
    10366377
    7936447
    9768943
    953678
    13475309
    EOD;	
}

?>
