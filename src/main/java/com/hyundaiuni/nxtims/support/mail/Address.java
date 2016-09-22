package com.hyundaiuni.nxtims.support.mail;

public class Address {
    private String address;
    private String name;

    /**
     * 메일 주소를 설정하는 생성자.
     * 
     * @param address 메일 주소
     */
    public Address(String address) {
        this.address = address;
    }

    /**
     * 메일 주소, 이름을 설정하는 생성자.
     * 
     * @param address 메일 주소
     * @param name 메일 이름
     */
    public Address(String address, String name) {
        this.address = address;
        this.name = name;
    }

    /**
     * 메일 주소를 리턴함.
     */
    public String getAddress() {
        return address;
    }

    /**
     * 메일 이름을 리턴함.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "address=" + address + ", name=" + name;
    }
}
