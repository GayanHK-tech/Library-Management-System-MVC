package Model;

public class Member {

    private String memberID;
    private String name;
    private String contactInfo;
    private MembershipCard membershipCard;

    public Member(String memberID, String name, String contactInfo, MembershipCard membershipCard) {
        this.memberID = memberID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.membershipCard = membershipCard;
    } // Getters and Setters

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public MembershipCard getMembershipCard() {
        return membershipCard;
    }

    public void setMembershipCard(MembershipCard membershipCard) {
        this.membershipCard = membershipCard;
    }

public void displayDetails() {
        System.out.println("Member ID: " + memberID);
        System.out.println("Name: " + name);
        System.out.println("Contact Info: " + contactInfo);
        membershipCard.displayCardDetails();
    }
}