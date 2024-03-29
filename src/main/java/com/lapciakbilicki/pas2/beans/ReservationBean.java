package com.lapciakbilicki.pas2.beans;

import com.lapciakbilicki.pas2.model.account.Account;
import com.lapciakbilicki.pas2.model.reservation.Reservation;
import com.lapciakbilicki.pas2.model.sportsfacility.SportsFacility;
import com.lapciakbilicki.pas2.service.AccountService;
import com.lapciakbilicki.pas2.service.ReservationService;
import com.lapciakbilicki.pas2.service.SportsFacilityService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Named
@FlowScoped("reservation")
public class ReservationBean implements Serializable {

    private Account account;
    private SportsFacility sportsFacility;
    private String startDate, startHour, endDate, endHour, message;
    private int isCreate;

    @Inject
    private FacesContext facesContext;
    @Inject
    private AccountService accountService;
    @Inject
    private SportsFacilityService sportsFacilityService;
    @Inject
    private ReservationService reservationService;

    @PostConstruct
    public void init() {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/y", Locale.US);
        this.startDate = sdf.format(now);
        this.endDate = this.startDate;
        sdf = new SimpleDateFormat("H");
        this.startHour = sdf.format(now);
        this.endHour = this.startHour;
        this.isCreate = 0;
    }

    public void addAccount(String id) {
        this.account = accountService.get(id);
    }

    public void addSportFacility(String id) {
        this.sportsFacility = sportsFacilityService.get(id);
    }

    public void addSportFacilityAndUser(String id) {
        facesContext = FacesContext.getCurrentInstance();
        String login = facesContext.getExternalContext().getRemoteUser();

        this.addSportFacility(id);
        this.addAccount(accountService.getAccountByLogin(login).getId());
    }

    public List<Reservation> accountReservations() {
        return reservationService.getAll()
                .stream()
                .filter(res -> res.getAccount().getId().equals(this.account.getId()))
                .collect(Collectors.toList());
    }

    public boolean check() {
        Date parseEndDate = null, parseStartDate = null, parseNowDate = null;
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/y HH:mm", Locale.US);
        String nowDate = sdf.format(now);
        try {
            parseStartDate = new SimpleDateFormat("d/M/y HH:mm", Locale.US)
                    .parse(this.startDate + " " + this.startHour + ":00");
            parseEndDate = new SimpleDateFormat("d/M/y HH:mm", Locale.US)
                    .parse(this.endDate + " " + this.endHour + ":00");
            parseNowDate = new SimpleDateFormat("d/M/y HH:mm", Locale.US)
                    .parse(nowDate);
            if (parseStartDate.before(parseNowDate)) {
                return false;
            }
        } catch (ParseException e) {
            e.getMessage();
            return false;
        }
        if (parseStartDate.equals(parseEndDate) || parseStartDate.after(parseEndDate)) {
            return false;
        }
        List<Reservation> list = reservationService.getAll()
                .stream()
                .filter(res -> res.getFacility().getId().equals(this.sportsFacility.getId()))
                .collect(Collectors.toList());
        for (Reservation item : list) {
            if (parseStartDate.after(item.getStartDate()) && parseStartDate.before(item.getEndDate())
                    || parseEndDate.after(item.getStartDate()) && parseEndDate.before(item.getEndDate())
                    || parseStartDate.equals(item.getStartDate())
                    || parseEndDate.equals(item.getEndDate())) {
                return false;
            }
        }
        return true;
    }

    public void createReservation() {
        try {
            if (this.check()) {
                Date parseStartDate = new SimpleDateFormat("d/M/y HH:mm", Locale.US)
                        .parse(this.startDate + " " + this.startHour + ":00");
                Date parseEndDate = new SimpleDateFormat("d/M/y HH:mm", Locale.US)
                        .parse(this.endDate + " " + this.endHour + ":00");

                Reservation reservation = new Reservation(
                        UUID.randomUUID().toString(),
                        account,
                        sportsFacility,
                        parseStartDate,
                        parseEndDate,
                        true
                );

                if (reservation.getAccount() != null && reservation.getFacility() != null) {
                    this.reservationService.add(reservation);
                    this.isCreate = 1;
                }
            } else {
                this.isCreate = -1;
            }

        } catch (ParseException e) {
            e.getMessage();
        }
    }

    public String getReturnValue() {
        return "/showReservations";
    }

    //<editor-fold desc="getters and setter">
    public int getIsCreate() {
        return isCreate;
    }

    public void setIsCreate(int isCreate) {
        this.isCreate = isCreate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public SportsFacility getSportsFacility() {
        return sportsFacility;
    }

    public void setSportsFacility(SportsFacility sportsFacility) {
        this.sportsFacility = sportsFacility;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    //</editor-fold>
}
