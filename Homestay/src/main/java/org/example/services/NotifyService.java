package org.example.services;

import org.example.models.Notify;
import org.example.models.User;
import org.example.untils.AppUtils;
import org.example.untils.CRUD;
import org.example.untils.SerializationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotifyService implements CRUD<Notify> {
    private static int nextIdNotify;
    public static String fileName = "notify.txt";
    public List<Notify> notifyList;
    private static NotifyService INSTANCE_NOTIFY_SERVICE;
    public static NotifyService getInstance() {
        if (INSTANCE_NOTIFY_SERVICE == null) {
            INSTANCE_NOTIFY_SERVICE = new NotifyService();
        }
        return INSTANCE_NOTIFY_SERVICE;
    }

    public NotifyService() {
        notifyList = (List<Notify>) SerializationUtil.deserialize(fileName);
        if (notifyList.isEmpty()) {
            notifyList = new ArrayList<>();
        }
        nextIdNotify = AppUtils.findNext(notifyList.stream().map(Notify::getId).collect(Collectors.toList()));
    }

    @Override
    public void save() {
        SerializationUtil.serialize(notifyList, fileName);
    }

    @Override
    public Notify create(Notify notify) {
        notify.setId(nextIdNotify);
        nextIdNotify++;
        notifyList.add(notify);
        save();
        return notify;
    }

    @Override
    public void delete(Notify notify) {
        Notify notifyDelete = null;
        for (Notify notify1 : notifyList) {
            if (notify1.getId() == (notify.getId())) {
                notifyDelete = notify1;
                break;
            }
        }
        notifyList.remove(notifyDelete);
        save();
    }


    @Override
    public void update(Notify notify) {
        for (Notify notify1 : notifyList) {
            if (notify1.getId() == notify.getId()) {
                notify1.setUser(notify.getUser());
                notify1.setName(notify.getName());
                notify1.setComment(notify.getComment());
                notify1.setStatus(notify.getStatus());
                notify1.setUser(notify.getUser());
                notify1.setPriceDiscount(notify.getPriceDiscount());
                notify1.setTimeStart(notify.getTimeStart());
                notify1.setTimeEnd(notify.getTimeEnd());
                break;
            }
        }
        save();
    }

    @Override
    public List<Notify> readFile() {
        return (List<Notify>) SerializationUtil.deserialize(fileName);
    }


    @Override
    public Notify find(int idNotify) {
        if (notifyList != null) {
            return notifyList.stream()
                    .filter(e->e.getId() == idNotify)
                    .findFirst()
                    .get();
        }
        return new Notify();
    }
    public List<Notify> find(User user) {
        if (notifyList != null) {
            return notifyList.stream()
                    .filter(notify -> notify.getUser().stream().anyMatch(user1 -> user.getId() == user1.getId()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    public void showNotify(List<Notify> notifys) {
        if (notifys != null && ! notifys.isEmpty()) {
            for (Notify notify : notifys) {
                String info = "Notify ID: " + notify.getId() +
                        ", Notify Name: " + notify.getName() +
                        ", Date send: " + notify.getTimeStart() ;

                System.out.println(info);
            }
        } else {
            System.out.println("No notify data available.");
        }
    }
}
