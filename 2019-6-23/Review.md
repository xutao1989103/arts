CAP理论：http://robertgreiner.com/2014/08/cap-theorem-revisited/

Consistency 一致性，每次请求获取到最新数据，如果数据没有同步可以返回错误响应
Availability 可用性，不会有错误响应，如果数据没有同步，返回之前的数据
Partition Tolerance 分区容错性，任意的分区网络故障，系统仍然能正常工作