package com.bananaplan.workflowandroid.data;

import android.content.Context;

import com.bananaplan.workflowandroid.R;
import com.bananaplan.workflowandroid.data.equipment.MaintenanceRecord;
import com.bananaplan.workflowandroid.data.worker.attendance.LeaveData;
import com.bananaplan.workflowandroid.data.worker.status.BaseData;
import com.bananaplan.workflowandroid.data.worker.status.DataFactory;
import com.bananaplan.workflowandroid.data.worker.status.FileData;
import com.bananaplan.workflowandroid.data.worker.status.HistoryData;
import com.bananaplan.workflowandroid.data.worker.status.PhotoData;
import com.bananaplan.workflowandroid.data.worker.status.RecordData;
import com.bananaplan.workflowandroid.data.Warning.WarningStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ben on 2015/7/18.
 */
public final class WorkingData {

    private static final String TAG = "WorkingData";

    private volatile static WorkingData sWorkingData = null;
    private static long sRandomId = 0;

    private Context mContext;

    private HashMap<Long, Manager> mManagersMap = new HashMap<>();
    private HashMap<Long, WorkerItem> mWorkersMap = new HashMap<>();
    private HashMap<Long, Vendor> mVendorsMap = new HashMap<>();
    private HashMap<Long, TaskItem> mTaskItemsMap = new HashMap<>();
    private HashMap<Long, TaskCase> mTaskCaseMap = new HashMap<>();
    private HashMap<Long, Equipment> mEquipmentsMap = new HashMap<>();
    private HashMap<Long, Factory> mFactoriesMap = new HashMap<>();


    public static WorkingData getInstance(Context context) {
        if (sWorkingData == null) {
            synchronized (WorkingData.class) {
                if (sWorkingData == null) {
                    sWorkingData = new WorkingData(context);
                }
            }
        }
        return sWorkingData;
    }

    private WorkingData(Context context) {
        mContext = context;
        generateFakeData(); // +++ only for test case
    }


    public ArrayList<Manager> getManagers() {
        return new ArrayList<>(mManagersMap.values());
    }
    public ArrayList<WorkerItem> getWorkers() {
        return new ArrayList<>(mWorkersMap.values());
    }
    public ArrayList<Factory> getFactories() {
        return new ArrayList<>(mFactoriesMap.values());
    }
    public ArrayList<Vendor> getVendors() {
        return new ArrayList<>(mVendorsMap.values());
    }
    public ArrayList<TaskCase> getTaskCases() {
        return new ArrayList<>(mTaskCaseMap.values());
    }
    public ArrayList<Equipment> getEquipments() {
        return new ArrayList<>(mEquipmentsMap.values());
    }


    public ArrayList<TaskItem> getTaskItemsByWorker(WorkerItem worker) {
        ArrayList<TaskItem> tmp = new ArrayList<>();
        if (worker == null) return tmp;
        ArrayList<TaskItem> orig = new ArrayList<>(mTaskItemsMap.values());
        for (TaskItem item : orig) {
            if (item.workerId == worker.id) {
                tmp.add(item);
            }
        }
        return tmp;
    }
    public ArrayList<TaskItem> getTaskItemsByEquipment(Equipment equipment) {
        ArrayList<TaskItem> tmp = new ArrayList<>();
        if (equipment == null) return tmp;
        ArrayList<TaskItem> orig = new ArrayList<>(mTaskItemsMap.values());
        for (TaskItem item : orig) {
            if (item.equipmentId == equipment.id) {
                tmp.add(item);
            }
        }
        return tmp;
    }


    public Manager getManagerById(long managerId) {
        return mManagersMap.get(managerId);
    }
    public TaskCase getTaskCaseById(long taskCaseId) {
        return mTaskCaseMap.get(taskCaseId);
    }
    public Vendor getVendorById(long vendorId) {
        return mVendorsMap.get(vendorId);
    }
    public WorkerItem getWorkerItemById(long workerId) {
        return mWorkersMap.get(workerId);
    }
    public TaskItem getTaskItemById(long taskId) {
        return mTaskItemsMap.get(taskId);
    }
    public Equipment getEquipmentById(long equipmentId) {
        return mEquipmentsMap.get(equipmentId);
    }
    public Factory getFactoryById(long factoryId) {
        return mFactoriesMap.get(factoryId);
    }


    public void updateWorkerItemInTaskItem(long taskItemId, long workerId) {
        mTaskItemsMap.get(taskItemId).workerId = workerId;
    }

    public void addRecordToWorker(WorkerItem worker, BaseData data) {
        if (worker == null || data == null) return;
        worker.records.add(data);
    }

    public long getLoginWorkerId() { // TODO
        return getRandomWorkerId();
    }

    // +++ only for test case
    private void generateFakeData() {
        final int managerCount = 5;
        final int factoryCount = 3;
        final int workerCount = 20;
        final int vendorCount = 3;
        final int taskCaseCount = 3;
        final int taskItemCount = 10;
        final int equipmentCount = 10;

        for (int i = 0 ; i < managerCount ; i++) {
            long managerId = getRandomId();
            mManagersMap.put(managerId, new Manager(managerId, "Manager " + i));
        }

        for (int i = 1; i <= factoryCount; i++) {
            Factory factory = new Factory(i, "Factory" + i);
            mFactoriesMap.put(factory.id, factory);
            for (int j = 1; j <= workerCount; j++) {
                WorkerItem workItem = new WorkerItem(mContext, j, "Worker" + j, "Title" + j);
                workItem.factoryId = factory.id;
                factory.workerItems.add(workItem);
                mWorkersMap.put(workItem.id, workItem);
            }
        }

        for (int i = 0; i < equipmentCount; i++) {
            Equipment equipment = new Equipment(i, "Equipment" + i, getRandomFactoryId());
            equipment.purchaseDate = getRandomDate();
            equipment.records.add(new MaintenanceRecord("reason1", getRandomDate()));
            equipment.records.add(new MaintenanceRecord("reason2", getRandomDate()));
            mEquipmentsMap.put(equipment.id, equipment);
        }

        for (Factory factory : mFactoriesMap.values()) {
            for (WorkerItem worker : factory.workerItems) {
                FileData file = (FileData) DataFactory.genData(worker.id, BaseData.TYPE.FILE);
                file.uploader = getRandomWorkerId();
                file.time = getRandomDate();
                file.fileName = "test.pdf";
                worker.records.add(file);
                HistoryData history1 = (HistoryData) DataFactory.genData(worker.id, BaseData.TYPE.HISTORY);
                history1.time = getRandomDate();
                history1.status = HistoryData.STATUS.WORK;
                worker.records.add(history1);
                HistoryData history2 = (HistoryData) DataFactory.genData(worker.id, BaseData.TYPE.HISTORY);
                history2.time = getRandomDate();
                history2.status = HistoryData.STATUS.OFF_WORK;
                worker.records.add(history2);
                PhotoData photo = (PhotoData) DataFactory.genData(worker.id, BaseData.TYPE.PHOTO);
                photo.time = getRandomDate();
                photo.uploader = getRandomWorkerId();
                photo.fileName = "test.png";
                photo.photo = mContext.getDrawable(R.drawable.drawer_equipment);
                worker.records.add(photo);
                RecordData record = (RecordData) DataFactory.genData(worker.id, BaseData.TYPE.RECORD);
                record.time = getRandomDate();
                record.reporter = getRandomWorkerId();
                record.description = "test description";
                worker.records.add(record);

                LeaveData leave1 = new LeaveData();
                leave1.date = getRandomDate();
                leave1.reason = "test reason";
                leave1.type = LeaveData.TYPE.PRIVATE;
                worker.leaveDatas.add(leave1);
                LeaveData leave2 = new LeaveData();
                leave2.date = getRandomDate();
                leave2.reason = "test reason";
                leave2.type = LeaveData.TYPE.SICK;
                worker.leaveDatas.add(leave2);
                LeaveData leave3 = new LeaveData();
                leave3.date = getRandomDate();
                leave3.reason = "test reason";
                leave3.type = LeaveData.TYPE.WORK;
                worker.leaveDatas.add(leave3);
            }
        }

        for (int i = 1; i <= vendorCount; i++) {
            Vendor vendor = new Vendor(i, "Vendor" + i);
            mVendorsMap.put(vendor.id, vendor);
            for (int j = 1; j <= taskCaseCount; j++) {
                TaskCase taskCase = new TaskCase(j, "Case" + j);
                mTaskCaseMap.put(taskCase.id, taskCase);
                taskCase.vendorId = vendor.id;
                taskCase.workerId = getRandomWorkerId();
                taskCase.materialPurchasedDate = getRandomDate();
                taskCase.deliveredDate = getRandomDate();
                taskCase.layoutDeliveredDate = getRandomDate();
                vendor.taskCases.add(taskCase);
                for (int k = 1; k <= taskItemCount; k++) {
                    TaskItem taskItem = new TaskItem(100 * i + 10 * j + k, "Item" + k);
                    taskItem.status = getRandomStatus();
                    if (taskItem.status != TaskItem.Status.NOT_START) {
                        taskItem.startDate = getRandomDate();
                        if (taskItem.status == TaskItem.Status.FINISH) {
                            taskItem.finishDate = getRandomDate();
                        }
                    }
                    taskItem.taskCaseId = taskCase.id;
                    Warning w1 = new Warning("No power", WarningStatus.SOLVED);
                    Warning w2 = new Warning("No power", WarningStatus.SOLVED);
                    Warning w3 = new Warning("No resource", WarningStatus.UNSOLVED);
                    Warning w4 = new Warning("No resource", WarningStatus.UNSOLVED);
                    w1.taskItemId = taskItem.id;
                    w2.taskItemId = taskItem.id;
                    w3.taskItemId = taskItem.id;
                    w4.taskItemId = taskItem.id;
                    w1.handle = getRandomWorkerId();
                    w2.handle = getRandomWorkerId();
                    w3.handle = getRandomWorkerId();
                    w4.handle = getRandomWorkerId();
                    taskItem.warningList.add(w1);
                    taskItem.warningList.add(w2);
                    taskItem.warningList.add(w3);
                    taskItem.warningList.add(w4);
                    taskCase.taskItems.add(taskItem);
                    mTaskItemsMap.put(taskItem.id, taskItem);
                    taskItem.equipmentId = getRandomEquipmentId();
                    taskItem.workerId = getRandomWorkerId();
                }
            }
        }
    }

    private TaskItem.Status getRandomStatus() {
        TaskItem.Status[] statuses = TaskItem.Status.values();
        int idx = (int) (Math.random() * statuses.length);
        return statuses[idx];
    }

    private long getRandomFactoryId() {
        int num = (int) (Math.random() * mFactoriesMap.keySet().size());
        List<Long> list = new ArrayList<>(mFactoriesMap.keySet());
        if (list.size() == 0) return 0;
        return list.get(num);
    }

    private long getRandomWorkerId() {
        int num = (int) (Math.random() * mWorkersMap.keySet().size());
        List<Long> list = new ArrayList<>(mWorkersMap.keySet());
        if (list.size() == 0) return 0;
        return list.get(num);
    }

    private long getRandomEquipmentId() {
        int num = (int) (Math.random() * mEquipmentsMap.keySet().size());
        List<Long> list = new ArrayList<>(mEquipmentsMap.keySet());
        if (list.size() == 0) return 0;
        return list.get(num);
    }

    private Date getRandomDate() {
        int year = randBetween(2014, 2015);
        int month = randBetween(0, Calendar.getInstance().get(Calendar.MONTH) - 1);
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
        gc.set(year, month, day);
        return gc.getTime();
    }

    private int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    private long getRandomId() {
        sRandomId++;
        return sRandomId;
    }
    // --- only for test case
}
