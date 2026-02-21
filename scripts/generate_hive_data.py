#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成 Hive 海量分析数据 CSV
用法：python generate_hive_data.py [行数]
默认生成 50000 行，输出到 backend/src/main/resources/hive/analytic_data.csv
"""
import sys
import random
from datetime import datetime

def main():
    n = int(sys.argv[1]) if len(sys.argv) > 1 else 50000
    out_path = "backend/src/main/resources/hive/analytic_data.csv"
    depts = [101, 102, 103, 104]
    categories = list(range(1, 9))
    years = [2021,2022, 2023, 2024, 2025]
    months = list(range(1, 13))

    with open(out_path, 'w', encoding='utf-8') as f:
        f.write("id,category_id,dept_id,value,period,create_time\n")
        for i in range(1, n + 1):
            cid = categories[(i - 1) % 8]
            dept = depts[(i - 1) % 4]
            val = round(50 + (i % 45) + (i % 7) * 0.5, 2)
            y = years[(i - 1) % 5]
            m = months[(i - 1) % 12]
            period = f"{y}{m:02d}"
            ts = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            f.write(f"{i},{cid},{dept},{val},{period},{ts}\n")
    print(f"Generated {n} rows -> {out_path}")

if __name__ == "__main__":
    main()
